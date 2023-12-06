package com.order.domain.vo;

import com.order.domain.entity.Order;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class OrderDetail extends Order {
    /**
     * 电影院名
     */
    private Integer movieName;
    /**
     * 电影id
     */
    private Integer movieId;

    @ApiParam("页码")
    private Integer page = 1;
    @ApiParam("一页数据")
    private Integer size = 10;
}
