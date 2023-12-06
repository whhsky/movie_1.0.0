package com.movie.movie.mapper;

import com.movie.domain.entity.Favorite;
import com.movie.domain.entity.MovieImages;
import com.movie.domain.entity.Movies;
import com.movie.domain.entity.News;
import com.movie.domain.vo.CommentDetailVo;
import com.movie.domain.vo.MoviesDetailVo;
import com.movie.domain.vo.MoviesPageVo;
import com.movie.domain.vo.NewsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MovieMapper {
    // 获取轮播图片
    List<MovieImages> getMovieBannerImage(Integer bannerCode);

    // 获取电影
    List<MoviesDetailVo> movieList(MoviesPageVo moviesPageVo);

    // Top100
    List<MoviesDetailVo> top100();

    // 电影详情根据id
    MoviesDetailVo getMovieDetailById(Integer id);

    // 获取电影图集
    List<MovieImages> getMovieImages(Integer movieId);

    // 收藏电影
    void createMovieFavorite(Favorite favorite);

    // 获取收藏
    Favorite getMovieFavorite(@Param("movieId")Integer movieId,  @Param("userId")Integer userId);

    // 删除收藏电影
    void delMovieFavorite(@Param("movieId") Integer movieId, @Param("userId")Integer userId);

    // 删除电影评论
//    void delMovieComment(Integer movieId, Integer userId);

    // 获取热点
    List<News> hotList(NewsVo newsVo);

    // 获取影院上映的电影信息
    List<MoviesDetailVo> videoCallMovies(Integer cinemaId);

    List<Movies> userCollectMovie(Integer id);


}
