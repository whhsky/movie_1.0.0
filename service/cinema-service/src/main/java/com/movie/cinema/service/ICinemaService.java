package com.movie.cinema.service;

import com.cinema.domain.entity.CinemaAndMovies;
import com.cinema.domain.vo.CinemaVo;
import com.movie.utils.DataGridView;

public interface ICinemaService {
    DataGridView cinemaList(CinemaVo cinemaVo);

    DataGridView videoCall(CinemaAndMovies cinemaAndMovies);

    DataGridView videoSeatView(Long viewId);
}
