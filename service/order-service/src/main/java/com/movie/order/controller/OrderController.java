package com.movie.order.controller;

import com.order.domain.dto.OrderDto;
import com.order.domain.entity.MyJsonTypeHandler;
import com.order.domain.vo.OrderDetail;
import com.movie.order.service.IOrderService;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import com.movie.utils.argumentResolverConfig.ParameterConvert;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@ApiOperation("订单接口")
@Validated
@RestController
@RequestMapping("/app/order")
public class OrderController {

    @Autowired
    private IOrderService OrderService;

    @ApiOperation("创建订单")
    @PostMapping("/order")
    @ParameterConvert
    public DataGridView createOrder(@RequestBody OrderDto orderDto){
        List<List<Integer>> seatList = MyJsonTypeHandler.StringToList(orderDto.getSeats());
        if (orderDto.getViewId() == null || seatList == null || seatList.size() > 4) return Utils.resFailure(400, "");
        return OrderService.createOrder(orderDto.getViewId(),seatList);
    }

    @ApiOperation("查询订单")
    @GetMapping("/order")
    @ParameterConvert
    public DataGridView getUserOrderList(OrderDetail order){
        order.setUserId(null);
        return OrderService.getUserOrderList(order);
    }

//    @ApiOperation("删除订单")
//    @DeleteMapping("/order")
//    @ParameterConvert
//    public DataGridView DeleteOrder(@RequestBody Map<String, String> orderMap){
//        String orderId = orderMap.get(translate("orderId"));
//        if (orderId == null) return Utils.resFailure(400, "");
//        return OrderService.DeleteOrder(Long.valueOf(orderId));
//    }

    @ApiOperation("支付订单")
    @PostMapping("/order_pay")
    @ParameterConvert
    public DataGridView orderPay(@RequestBody OrderDetail order){
        order.setUserId(null);
        return OrderService.orderPay(order);
    }

}
