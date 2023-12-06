package com.cinema.domain.entity;

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
/**
 * 影厅座位信息(Seat)实体类
 *
 * @author makejava
 * @since 2023-07-30 16:45:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class)
public class Seat implements Serializable {
    private static final long serialVersionUID = 589881730468491157L;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 座位行号
     */
    private Integer seatRow;
    /**
     * 座位列号
     */
    private Integer seatColumn;
    /**
     * 座位状态（-1 停用或没有 / 1 正常使用）
     */
    private Integer seatStatus;
    /**
     * 影院id
     */
    private Integer cinemaId;
    /**
     * 影厅id
     */
    private Integer videoCallId;
    /**
     * 电影在电影院上映id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long mcId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除时间
     */
    private Date deleteTime;

}

