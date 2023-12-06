package com.movie.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.movie.domain.entity.Movies;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class) // 将下划线映射为驼峰
public class MoviesDetailVo extends Movies {

    private String dictLabels;
    /**
     * 电影类型
     */
    private String typeLabel;
    /**
     * 电影区域
     */
    private String regionLabel;
    /**
     * 电影年代
     */
    private String eraLabel;
    /**
     * 电影状态
     */
    private String statusLabel;
    /**
     * 电影演职人员
     */
    private String starring;
}
