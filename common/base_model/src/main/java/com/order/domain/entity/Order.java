package com.order.domain.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单实体(Order)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class)
public class Order implements Serializable {
    private static final long serialVersionUID = 313789675264879588L;

    /**
     * 未支付
     */
    private static int PAY_ZERO = 0;
    /**
     * 已支付
     */
    private static int PAY_ONE = 1;

    private Integer id;
    /**
     * 订单id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long orderId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 订单价格
     */
    private Double orderPrice;
    /**
     * 电影上放映室id 关联(电影id,影院id,放映室id)
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long moviesCinemaId;
    /**
     * 座位信息
     */
    private List<List<Integer>> seatIds;
    /**
     * 是否已支付
     */
    private Integer orderIfPay;
    /**
     * seatId唯一标识
     */
    private String seatIdList;
    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payTime;
    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deleteTime;
}