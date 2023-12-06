package com.movie.movie.service;


import com.movie.domain.entity.Comment;
import com.movie.domain.entity.Favorite;
import com.movie.domain.vo.CommentVo;
import com.movie.domain.vo.MoviesPageVo;
import com.movie.domain.vo.NewsVo;
import com.movie.utils.DataGridView;

public interface IMovieService {
    // 获取电影轮播图
    DataGridView getBanner(Integer bannerCode);

    // 获取电影图集
    DataGridView getMovieImages(Integer movieId);
    // 获取电影
    DataGridView movieList(MoviesPageVo moviesPageVo);

    // 首页排行
    DataGridView rankList();

    // 地区排行
    DataGridView movieRegionRank(String selectType, String movieRegion);

    // top100
    DataGridView top100(String page, String size);

    // 电影详细根据id
    DataGridView movieDetail(Integer id);

    DataGridView getMovieComment(CommentVo commentVo);

    DataGridView movieComment(Comment comment);

    DataGridView createMovieFavorite(Favorite favorite);
    DataGridView ifMovieFavorite(Integer movieId);
    DataGridView delMovieFavorite(Favorite favorite);

    // 删除电影评论
    // DataGridView delMovieComment(Comment comment);
    DataGridView hotList(NewsVo newsVo);

    DataGridView videoCallMovies(Integer cinemaId);

    DataGridView userCollectMovies(MoviesPageVo moviesPageVo);

    DataGridView userCommentMovies(CommentVo commentVo);
}
