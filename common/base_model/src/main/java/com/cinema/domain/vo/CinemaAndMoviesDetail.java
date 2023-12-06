package com.cinema.domain.vo;

import com.cinema.domain.entity.Cinema;
import com.cinema.domain.entity.CinemaAndMovies;
import lombok.Data;

@Data
public class CinemaAndMoviesDetail extends CinemaAndMovies {
    /**
     * 影厅行数
     */
    private Integer videoRow;
    /**
     * 影厅列数
     */
    private Integer videoColumn;

    /**
     * 影院信息
     */
    private Cinema CinemaInfo;

}
