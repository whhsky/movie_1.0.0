package com.movie.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * (Favorite)实体类
 *
 * @author makejava
 * @since 2023-07-26 19:46:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class) // 将下划线映射为驼峰
public class Favorite implements Serializable {
    private static final long serialVersionUID = 440390083648377766L;

    /**
     * 收藏id
     */
    private Integer id;

    /**
     * 更新时间
     */
    private Date createTime;

    /**
     * 收藏的电影
     */
    private Integer movieId;

    /**
     * 用户id
     */
    private Integer userId;


}

