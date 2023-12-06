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
import java.util.Date;
/**
 * (Cinema)实体类
 *
 * @author makejava
 * @since 2023-07-25 17:11:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class)
public class Cinema implements Serializable {
    private static final long serialVersionUID = -74572600442793112L;

    private Integer id;

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

    @ApiParam("影院详细地址")
    private String  cinemaAddress;

    @ApiParam("影院名称")
    private String  cinemaName;

    @ApiParam("影院品牌(关联基础数据表)")
    private Integer  cinemaBrand;

    @ApiParam("影院区域(关联基础数据表)")
    private Integer  cinemaReg;

    @ApiParam("影院类型(关联基础数据表)")
    private String  cinemaType;

    @ApiParam("影院服务(关联基础数据表)")
    private Integer  cinemaService;

    @ApiParam("平均价格")
    private Double   cinemaAvgPrice;

    @ApiParam("影院图片")
    private String   cinemaImage;

    @ApiParam("影院折扣卡")
    private Integer  cinemaZkk;

    @ApiParam("影院距离")
    private Double   cinemaJl;

    @ApiParam("影院电话")
    private String cinemaPhone;
}

