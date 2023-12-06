package com.movie.cast.service;

import com.movie.utils.DataGridView;

public interface ICastService {
    DataGridView getMovieCast(Integer  movieId);
}
