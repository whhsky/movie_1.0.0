package com.movie.order.service.impl;

import com.cinema.domain.entity.Seat;
import com.cinema.domain.vo.CinemaAndMoviesDetail;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.movie.cinema.client.CinemaFeignClient;
import com.movie.domain.vo.MoviesDetailVo;
import com.movie.movie.client.MovieFeignClient;
import com.movie.order.mapper.OrderMapper;
import com.movie.order.service.IOrderService;
import com.movie.secutity.UserUtils;
import com.movie.secutity.domain.RedisUser;
import com.movie.user.client.UserFeignClient;
import com.movie.utils.DataGridView;
import com.movie.utils.RedisUtils;
import com.movie.utils.SnowFlake;
import com.movie.utils.Utils;
import com.order.domain.entity.Order;
import com.order.domain.vo.OrderDetail;
import com.rabbitmq.client.Channel;
import com.user.domain.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

import static com.movie.utils.rabbitmqConfig.rabbitmqConfig.*;


@Slf4j
@Service
public class IOrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CinemaFeignClient cinemaFeignClient;

    @Autowired
    private MovieFeignClient movieFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(
                (correlationData, ack, cause) ->{
                    if (ack){
                        log.info("消息正确到达！id:{}", correlationData.getId());
                        return;
                    }
                    log.error("消息未正确到达！原因为:{}", cause);
                }
        );

        rabbitTemplate.setReturnCallback(
                new RabbitTemplate.ReturnCallback() {
                    @Override
                    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                        log.error("消息未正确到路由！原因为:{}", new String(message.getBody()));
                    }
                }
        );

    }

    /**
     * 发送order消息
     */
    public void sendMsg(Long orderId, Integer userId){
        String msg = orderId.toString() + "," + userId.toString();
//        Message message = MessageBuilder.withBody(msg.getBytes()).setDeliveryTag(orderId).build();
        Message message = MessageBuilder.withBody(msg.getBytes()).build();
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE, BINDING_NORMAL_KEY, message, new CorrelationData(msg));
        log.info("消息一已发送，发送时间:{}", new Date());
    }

    /**
     * 接收死信队列order消息
     */
    // 重试次数
    private int retryCount = 0;

    @RabbitListener(queues = {DEAD_QUEUE})
    public void receiveMsg(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            byte[] body = message.getBody();
            String[] msg = new String(body).split(",");
            String orderId = msg[0];
            String userId = msg[1];
            boolean b = redisUtils.stnx(orderId);
            if (b){ // 判断指定的msg是否存在(不存在返回True)，如果不存在相当第一次消费
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(Long.valueOf(orderId));
                orderDetail.setUserId(Integer.parseInt(userId));
                Order orderInfo = orderMapper.getUserOrderList(orderDetail).get(0);
                if(orderInfo. getOrderIfPay() == 0){
                    // 订单超时未支付
                    // 取消锁定座位
                    for (String seatId : orderInfo.getSeatIdList().split(",")) {
//                        cinemaMapper.deleteSeats(seatId);
                        cinemaFeignClient.deleteSeats(seatId);
                    }
                    orderInfo.setOrderStatus(0); // 订单取消
                    Date date = new Date();
                    orderInfo.setUpdateTime(date);
                    orderMapper.updateOrder(orderInfo);  // 更新订单状态
                }
            }
            // 手动确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("在接受到的消息时出现异常:{}", e.getMessage());
            retryCount++;
            throw new RuntimeException("在接受到的消息时出现异常", e);
        }finally {
            if (retryCount == 3){
                byte[] body = message.getBody();
                String s = new String(body);
                log.error("消息{}出现异常，请人工处理！",s);
                channel.basicNack(deliveryTag, false, false);
                retryCount = 0;
            }
        }
    }


    /**
     * 创建订单
     * @param viewId
     * @param seat
     * @return
     */
    @Override
    @Transactional
    public DataGridView createOrder(Long viewId, List<List<Integer>> seat) {
        CinemaAndMoviesDetail videoView = cinemaFeignClient.getVideoView(viewId);
        RedisUser redisUser = UserUtils.getLoginUser();
        SysUser sysUser = redisUser.getSysUser();
        Date date = new Date();
        List<String> seatIdList = new ArrayList<>();
        // 设置座位信息
        try {
            for (List<Integer> integers : seat) {
                Integer seatRow = integers.get(0);
                Integer seatColumn = integers.get(1);
                Seat seatItem = cinemaFeignClient.getSeatDetail2(seatRow, seatColumn, videoView.getCinemaId(), videoView.getVideoCallId(), videoView.getId());
                if (!Objects.isNull(seatItem)) {
                    return Utils.resFailure(400, "该座位已被占坐！！！");
                } else {
                    Seat seatInfo = new Seat();
                    seatInfo.setSeatRow(seatRow);
                    seatInfo.setSeatColumn(seatColumn);
                    // 设置该座位状态
                    seatInfo.setSeatStatus(2);
                    seatInfo.setCinemaId(videoView.getCinemaId());
                    seatInfo.setVideoCallId(videoView.getVideoCallId());
                    seatInfo.setUserId(sysUser.getId());
                    seatInfo.setCreateTime(date);
                    seatInfo.setUpdateTime(date);
                    seatInfo.setMcId(videoView.getId());
                    // 创建座位
                    Long seatId  = cinemaFeignClient.createSeat(seatInfo);
                    seatIdList.add(String.valueOf(seatId));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            // 手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Utils.resFailure(400, "");
        }
        try {
            // 设置订单信息
            SnowFlake idWorker = new SnowFlake(0, 0);
            Long l = idWorker.nextId();
            Order order = new Order();
            order.setOrderId(l);
            order.setOrderPrice(videoView.getMoviePrice() * seat.size());
            order.setOrderIfPay(0);
            order.setMoviesCinemaId(videoView.getId());
            order.setUserId(sysUser.getId());
            order.setSeatIds(seat);
            order.setUserPhone(sysUser.getPhone());
            order.setSeatIdList(String.join(",",seatIdList));
            order.setCreateTime(date);
            order.setUpdateTime(date);
            orderMapper.createOrder(order);
            order.setUserId(null);
            sendMsg(l, sysUser.getId());
            return Utils.resSuccess(200, "", order);
        }catch (Exception e){
            e.printStackTrace();
            // 手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Utils.resFailure(400, "");
        }
    }

    /**
     * 支付订单
     */
    public DataGridView orderPay(OrderDetail orderDetail){
        // 1.从SecurityContextHolder.getContext().getAuthentication()中获取认证信息
        RedisUser redisUser = UserUtils.getLoginUser();
        SysUser sysUser = redisUser.getSysUser();
        orderDetail.setUserId(sysUser.getId());
        try {
            Double overage = userFeignClient.iPurse(sysUser.getId());
            Order order = orderMapper.getUserOrder(orderDetail);
            Double orderPrice = order.getOrderPrice();
            if(!(overage >= orderPrice)){
                return Utils.resFailure(400, "余额不足！！！");
            }
            if (1 == order.getOrderIfPay()){
                return Utils.resFailure(400, "订单已完成，无需再次支付！");
            }
            if(0 == order.getOrderStatus()){
                return Utils.resFailure(400, "订单已取消,禁止支付！！！");
            } else if(1 == order.getOrderStatus()){
                overage = overage - orderPrice;
                userFeignClient.setUserOverage(overage,sysUser.getId());
                Date date = new Date();
                order.setOrderIfPay(1);
                order.setUpdateTime(date);
                order.setPayTime(date);
                orderMapper.updateOrder(order);
                return Utils.resSuccess(200, "支付成功！", null);
            }else{
                return Utils.resFailure(400, "订单已退款！");
            }

        } catch (Exception e) {
            log.error("订单号{}在支付时出现异常!!!异常信息：{}", orderDetail.getOrderId(), e.getMessage());
            return Utils.resFailure(400, "支付失败！");
        }
    }


    /**
     * 查询用户订单,根据用户id查询 或订单号(id)查询
     * 整合影院信息，电影信息，放映室信息，订单信息
     * @param order
     * @return
     */
    @Override
    public DataGridView getUserOrderList(OrderDetail order) {
        Page<Object> page;
        List<Map<String,Object>> result;

        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            page = PageHelper.startPage(order.getPage(), order.getSize(), "-create_time");
            // 设置user_id
            order.setUserId(sysUser.getId());
            List<Order> userOrderList = orderMapper.getUserOrderList(order);
            result = new ArrayList<>();
            for (Order orderDetail : userOrderList) {
                orderDetail.setSeatIdList(null);
                Long viewId = orderDetail.getMoviesCinemaId();
                HashMap<String, Object> infoMap = new HashMap<>();
                CinemaAndMoviesDetail view = cinemaFeignClient.getVideoView(viewId);
                MoviesDetailVo movieDetail = movieFeignClient.getMovieDetail(view.getMovieId());
                for (String labels : movieDetail.getDictLabels().split(",")) {
                    String keyLabel = labels.split(":")[0];
                    String valueLabel = labels.split(":")[1];
                    switch (keyLabel){
                        case "movie_status": movieDetail.setStatusLabel(valueLabel); break;
                        case "movie_type": movieDetail.setTypeLabel(valueLabel); break;
                        case "movie_region": movieDetail.setRegionLabel(valueLabel); break;
                        case "movie_era": movieDetail.setEraLabel(valueLabel); break;
                        default: break;
                    }
                }
                movieDetail.setDictLabels(null);
                infoMap.put("order", orderDetail);
                infoMap.put("view_info", view);
                infoMap.put("movie_info", movieDetail);
                result.add(infoMap);
            }
            return Utils.pagSuccess(200, "", page.getTotal(), result);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.resFailure(400, "");
        }
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @Transactional
    @Override
    public DataGridView DeleteOrder(Long orderId) {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            OrderDetail order = new OrderDetail();
            order.setUserId(sysUser.getId());
            order.setOrderId(orderId);
            Order userOrder = orderMapper.getUserOrderList(order).get(0);
            orderMapper.DeleteOrder(order);
            for (String id : userOrder.getSeatIdList().split(",")) {
                cinemaFeignClient.deleteSeats(id);
            }
            return Utils.resSuccess(200, "删除订单成功！", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Utils.resFailure(400,"删除订单失败！");
        }
    }
}
