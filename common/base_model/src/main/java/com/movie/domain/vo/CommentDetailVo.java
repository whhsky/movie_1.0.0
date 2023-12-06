package com.movie.domain.vo;

import com.movie.domain.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommentDetailVo extends Comment {
    @ApiModelProperty(value = "用户昵称",hidden = true)
    private String userName;
    @ApiModelProperty(value = "用户头像",hidden = true)
    private String userIcon;

    @ApiModelProperty(value = "电影名称",hidden = true)
    private String movieName;

    @ApiModelProperty(value = "电影详情",hidden = true)
    private String movieDescription;

    @ApiModelProperty(value = "电影图片",hidden = true)
    private String moviePoster;

}
