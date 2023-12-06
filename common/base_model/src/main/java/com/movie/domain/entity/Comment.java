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
 * (Comment)实体类
 *
 * @author makejava
 * @since 2023-07-25 11:23:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class)
public class Comment implements Serializable {
    private static final long serialVersionUID = 370671113887901875L;

    @ApiModelProperty(hidden = true)
    private Integer id;

    @ApiParam("电影id/资讯id")
    private Integer movieId;

    @ApiModelProperty(value = "用户id",hidden = true)
    private Integer userId;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "删除时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deleteTime;

    @ApiParam("评分")
    private Double score;

    @ApiParam("评论内容")
    private String content;

    @ApiParam("评论类型 (0：电影/1：资讯)")
    private Integer commentType;

    @ApiParam("点赞数")
    private Integer support;

    @ApiParam("回复id")
    private Integer replyId;
}