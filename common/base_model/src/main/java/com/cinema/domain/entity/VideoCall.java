package com.cinema.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * 电影影厅(VideoCall)实体类
 *
 * @author makejava
 * @since 2023-07-30 16:45:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class)
public class VideoCall implements Serializable {
    private static final long serialVersionUID = -45757801479652686L;
    
    private Integer id;
    /**
     * 影厅id
     */
    private Integer videoCallId;
    /**
     * 影院id
     */
    private Integer cinemaId;
    /**
     * 影厅行数
     */
    private Integer videoRow;
    /**
     * 影厅列数
     */
    private Integer videoColumn;
    /**
     * 影厅类型(关联基础数据表)
     */
    private Integer videoType;
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

