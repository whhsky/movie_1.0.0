package com.movie.order.mapper;

import com.order.domain.entity.Order;
import com.order.domain.vo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderMapper {

    /**
     * 根据userId,orderId获取订单
     * @param order
     * @return
     */
    List<Order> getUserOrderList(OrderDetail order);

    /**
     * 单个订单
     * @param order
     * @return
     */
    Order getUserOrder(OrderDetail order);


    /**
     * 创建订单
     * @param order
     */
    void createOrder(Order order);

    /**
     * 删除订单
     * @param order
     */
    void DeleteOrder(OrderDetail order);

    void updateOrder(Order order);
}
