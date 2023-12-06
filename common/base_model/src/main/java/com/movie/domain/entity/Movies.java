package com.movie.domain.entity;

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
 * 电影(Movies)实体类
 *
 * @author makejava
 * @since 2023-07-10 15:56:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class) // 将下划线映射为驼峰
public class Movies implements Serializable {
    private static final long serialVersionUID = -52075297060621455L;
    /**
     * 电影id
     */
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
    /**
     * 电影名称
     */
    @ApiParam("电影名称")
    private String movieName;
    /**
     * 评分
     */
    private Double movieScore;
    /**
     * 海报
     */
    private String moviePoster;
    /**
     * 类型
     */
    @ApiParam("电影类型")
    private Integer movieType;
    /**
     * 区域
     */
    @ApiParam("电影区域")
    private Integer movieRegion;
    /**
     * 时长
     */
    private Integer movieDuration;
    /**
     * 上映时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date movieReleaseDate;
    /**
     * 年代
     */

    @ApiParam("电影年代")
    private Integer movieEra;
    /**
     * 剧情介绍
     */
    @ApiModelProperty(value = "剧情介绍",hidden = true)
    private String movieDescription;
    /**
     * 评分人数
     */
    private Long   movieScoreNum;
    /**
     * 票房
     */
    private Double movieBoxOffice;
    /**
     * 想看数
     */
    private Long   movieAnticipate;
    /**
     * 电影状态
     */
    @ApiParam("电影状态")
    private Integer movieStatus;


}

