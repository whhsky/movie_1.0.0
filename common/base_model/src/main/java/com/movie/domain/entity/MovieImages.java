package com.movie.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 电影图集(MovieImages)实体类
 *
 * @author makejava
 * @since 2023-07-10 16:04:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class)
public class MovieImages {
        private static final long serialVersionUID = -74985136353945645L;
        /**
         * 图片id
         */
        private Integer id;
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
        /**
         * 电影id
         */
        private String movieId;
        /**
         * 图片
         */
        private String image;

        /**
         * 轮播类型
         */
        private Integer rotationChart;
}
