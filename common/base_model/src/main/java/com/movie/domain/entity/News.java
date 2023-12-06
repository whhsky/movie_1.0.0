package com.movie.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * (News)实体类
 *
 * @author makejava
 * @since 2023-07-27 15:30:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class) // 将下划线映射为驼峰
public class News implements Serializable {
    private static final long serialVersionUID = -532390083648377788L;

    /**
     * 资讯id
     */
    private Integer id;

    /**
     * 资讯图片
     */
    private String newsImg;

    /**
     * 资讯标题
     */
    private String newsTitle;

    /**
     * 资讯内容
     */
    private String newsContent;

    /**
     * 资讯标签
     */
    private String newsTags;

    /**
     * 资讯浏览人数
     */
    private Long   newsView;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
