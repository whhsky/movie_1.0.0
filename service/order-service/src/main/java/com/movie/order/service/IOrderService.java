package com.movie.order.service;
import com.order.domain.vo.OrderDetail;
import com.movie.utils.DataGridView;

import java.util.List;

public interface IOrderService {
    DataGridView createOrder(Long viewId, List<List<Integer>> seat);

    DataGridView getUserOrderList(OrderDetail order);

    DataGridView DeleteOrder(Long orderId);

    DataGridView orderPay(OrderDetail order);
}
