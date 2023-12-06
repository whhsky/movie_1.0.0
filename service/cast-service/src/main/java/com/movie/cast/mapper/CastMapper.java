package com.movie.cast.mapper;

import com.cast.domain.entity.Cast;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CastMapper {
    List<Cast> getMovieCast(Integer  movieId);
}
