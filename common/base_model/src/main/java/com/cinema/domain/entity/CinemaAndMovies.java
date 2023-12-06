package com.cinema.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
/**
 * (CinemaAndMovies)实体类
 *
 * @author makejava
 * @since 2023-07-25 17:11:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class)
public class CinemaAndMovies implements Serializable {
    private static final long serialVersionUID = -84572600442791112L;

    private Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deleteTime;

    @ApiParam("电影id")
    private Integer  movieId;

    @ApiParam("影院id")
    private Integer  cinemaId;

    @ApiParam("开始放映时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private ZonedDateTime viewStartTime;

    @ApiParam("结束放映时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private ZonedDateTime  viewEndTime;

    @ApiParam("语言版本")
    private String  languageVersion;

    @ApiParam("放映厅")
    private Integer  videoCallId;

    @ApiParam("电影价格")
    private Double  moviePrice;

    @ApiParam("影厅类型")
    private String videoType;
}

