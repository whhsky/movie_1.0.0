package com.movie.movie.client;

import com.movie.domain.vo.MoviesDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "movie-service")
public interface MovieFeignClient {
    @GetMapping("/app/movies/inner/movie/{id}")
    MoviesDetailVo getMovieDetail(@PathVariable("id") Integer movieId);
}
