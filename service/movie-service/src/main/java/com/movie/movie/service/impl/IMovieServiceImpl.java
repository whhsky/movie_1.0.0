package com.movie.movie.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.movie.domain.entity.Comment;
import com.movie.domain.entity.Favorite;
import com.movie.domain.entity.Movies;
import com.movie.domain.entity.News;
import com.movie.domain.vo.*;
import com.movie.movie.mapper.CommentMapper;
import com.movie.movie.mapper.MovieMapper;
import com.movie.movie.service.IMovieService;
import com.movie.secutity.UserUtils;
import com.movie.secutity.domain.RedisUser;
import com.movie.sysdict.client.SysdictFeignClient;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import com.sysdict.domain.entity.SysDictData;
import com.user.domain.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IMovieServiceImpl implements IMovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private SysdictFeignClient sysdictFeignClient;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 获取轮播图片
     * @param bannerCode
     * @return
     */
    @Override
    public DataGridView getBanner(Integer bannerCode){
        try {
            return Utils.resSuccess(200, "",  movieMapper.getMovieBannerImage(bannerCode));
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
    }

    /**
     * 电影列表
     * @deprecated 排序 sort 0:按热门排序 /1：按时间排序/2：按评价排序
     * @deprecated 没有状态参数：根据上映时间倒序排序
     * @deprecated 有状态参数：判断是否上映
     * @deprecated 待上映电影根据热度和上映时间排序，热度依据为想看数
     * @deprecated 上映电影根据热度、上映时间和评分排序，热度依据为票房w
     */
    @Override
    public DataGridView movieList(MoviesPageVo moviesPageVo) {

        List<String> sortList = new ArrayList<>();

        if (moviesPageVo.getMovieStatus() != null){
            SysDictData sysDictData = sysdictFeignClient.SysDictData(moviesPageVo.getMovieStatus());
            if (sysDictData.getDictSort() == 1){
                sortList.add("-movie_anticipate");
                sortList.add("-movie_release_date");
            }else{
                sortList.add("-movie_box_office");
                sortList.add("-movie_release_date");
                sortList.add("-movie_score");
            }
        }else {
            sortList.add("-movie_release_date");
        }
        try {
            Page<Object> page = PageHelper.startPage(moviesPageVo.getPage(), moviesPageVo.getSize(), sortList.get(moviesPageVo.getMovieSort()));
            List<MoviesDetailVo> movies = movieMapper.movieList(moviesPageVo);
            return Utils.pagSuccess(200, "", page.getTotal(), movies);
        }catch (Exception e){
            return Utils.resFailure(200, "");
        }

    }

    /**
     * 排行榜列表
     * @return
     */
    @Override
    public DataGridView rankList() {

        Map<String, Object> resultMap = new HashMap<>();
        MoviesPageVo moviesPageVo = new MoviesPageVo();

        /**
         * @deprecated 最受期待榜
         */
        try {
            moviesPageVo.setMovieStatus(81);
            Map<String, Object> movie80 = new HashMap<>();
            Page<Object> page = PageHelper.startPage(moviesPageVo.getPage(), moviesPageVo.getSize(), "-movie_anticipate");
            List<MoviesDetailVo> movies = movieMapper.movieList(moviesPageVo);
            movie80.put("count", page.getTotal());
            movie80.put("movie_list", movies);
            resultMap.put("anticipate_list", movie80);
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
        /**
         * @deprecated 今日票房榜
         */
        try {
            moviesPageVo.setMovieStatus(80);
            Map<String, Object> movie81 = new HashMap<>();
            Page<Object> page = PageHelper.startPage(moviesPageVo.getPage(), moviesPageVo.getSize(), "-movie_box_office");
            List<MoviesDetailVo> movies = movieMapper.movieList(moviesPageVo);
            movie81.put("count", page.getTotal());
            movie81.put("movie_list", movies);
            resultMap.put("box_office_list", movie81);
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }

        return Utils.resSuccess(200, "", resultMap);
    }


    /**
     * 根据区域获取排行榜
     * @selectType 0 热映口碑榜； 1 票房榜; 2 期待人数榜
     * @param selectType, movieRegion, movieStatus
     * @return DataGridView
     */
    @Override
    public DataGridView movieRegionRank(String selectType, String movieRegion) {
        try {
            MoviesPageVo moviesPageVo = new MoviesPageVo();
            if ("0".equals(selectType)){
                moviesPageVo.setMovieStatus(80);
                // 按热映口碑排序
                PageHelper.startPage(moviesPageVo.getPage(), moviesPageVo.getSize(), "-movie_score, -movie_score_num, -movie_box_office");
            }else if("1".equals(selectType)){
                moviesPageVo.setMovieStatus(80);
                // 按票房排序
                PageHelper.startPage(moviesPageVo.getPage(), moviesPageVo.getSize(), "-movie_box_office, -movie_score, -movie_release_date");

            }else if("2".equals(selectType)){
                moviesPageVo.setMovieStatus(81);
                // 按期待人数排序
                PageHelper.startPage(moviesPageVo.getPage(), moviesPageVo.getSize(), "-movie_anticipate, -movie_release_date");
            }
            else {
                return Utils.resFailure(400, "");
            }
            if (movieRegion != null)
                moviesPageVo.setMovieRegion(Integer.parseInt(movieRegion));
            List<MoviesDetailVo> movies = movieMapper.movieList(moviesPageVo);
            return Utils.resSuccess(200, "",  movies);
        } catch (Exception e) {
            return Utils.resFailure(402, "");
        }
    }

    /**
     * @deprecated top100榜按票房、评分、评分人数排序
     * @param
     * @return DataGridView
     */
    @Override
    public DataGridView top100(String page, String size) {
        try {
            MoviesPageVo moviesPageVo = new MoviesPageVo();
            if (page != null || size != null){
                moviesPageVo.setPage(Integer.parseInt(page));
                moviesPageVo.setSize(Integer.parseInt(size));
            }
            Page<Object> page_list = PageHelper.startPage(moviesPageVo.getPage(), moviesPageVo.getSize(), "-movie_box_office, -movie_score, -movie_score_num");
            List<MoviesDetailVo> movies = movieMapper.top100();

            return Utils.pagSuccess(200, "", page_list.getTotal(), movies);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.resFailure(400,"");
        }
    }

    /**
     * 获取电影详情
     * @param id
     * @return
     */
    @Override
    public DataGridView movieDetail(Integer id) {
        try {
            MoviesDetailVo movieDetail = movieMapper.getMovieDetailById(id);
            for (String labels : movieDetail.getDictLabels().split(",")) {
                String keyLabel = labels.split(":")[0];
                String valueLabel = labels.split(":")[1];
                switch (keyLabel){
                    case "movie_status": movieDetail.setStatusLabel(valueLabel); break;
                    case "movie_type": movieDetail.setTypeLabel(valueLabel); break;
                    case "movie_region": movieDetail.setRegionLabel(valueLabel); break;
                    case "movie_era": movieDetail.setEraLabel(valueLabel); break;
                    default: break;
                }
            }
            movieDetail.setDictLabels(null);
            return Utils.resSuccess(200, "",  movieDetail);
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
    }

    /**
     * 获取电影图集
     * @param movieId
     * @return
     */
    @Override
    public DataGridView getMovieImages(Integer movieId) {
        try {
            return Utils.resSuccess(200, "",  movieMapper.getMovieImages(movieId));
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
    }

    /**
     * 获取电影影评
     * 排序字段CommentSort 0:按热门排序 /1：按时间排序
     * @param commentVo
     * @return
     */
    @Override
    public DataGridView getMovieComment(CommentVo commentVo) {
        try {
            Page<Object> page;
            if (commentVo.getCommentSort() == 0){
                 page = PageHelper.startPage(commentVo.getPage(), commentVo.getSize(), "-support, update_time, create_time");
            }else{
                 page = PageHelper.startPage(commentVo.getPage(), commentVo.getSize(), "-update_time, -create_time, -support");
            }
            List<CommentDetailVo> movieComment = commentMapper.getMovieComment(commentVo.getMovieId());
            return Utils.pagSuccess(200, "",  page.getTotal(), movieComment);
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
    }

    /**
     * 提交电影影评
     * @param comment
     * @return
     */
    @Override
    public DataGridView movieComment(Comment comment) {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            List<Comment> movieComment = commentMapper.ifGetMovieComment(comment.getMovieId(), sysUser.getId(), -1);
            if (movieComment.size() > 0) return Utils.resFailure(400, "您已经对该电影进行过影评,请勿重复影评！");
            Date date = new Date();
            comment.setCommentType(0);
            comment.setCreateTime(date);
            comment.setUpdateTime(date);
            comment.setUserId(sysUser.getId());
            commentMapper.createComment(comment);
            return Utils.resSuccess(200, "提交成功！", null);
        }catch (Exception e){
            return Utils.resFailure(400, "未知错误,提交失败!");
        }
    }

    /**
     * 删除评论
     */
//    @Override
//    public DataGridView delMovieComment(Comment comment) {
//        try {
//              RedisUser redisUser = UserUtils.getLoginUser();
//            SysUser sysUser = redisUser.getSysUser();
//            movieMapper.delMovieComment(comment.getMovieId(), sysUser.getId());
//            return Utils.resSuccess(200, "提交成功！", null);
//        } catch (Exception e) {
//            return Utils.resFailure(400, "未知错误,提交失败!");
//        }
//    }

    /**
     * 电影收藏
     * @param favorite
     * @return
     */
    @Override
    public DataGridView createMovieFavorite(Favorite favorite) {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            if(!Objects.isNull(movieMapper.getMovieFavorite(favorite.getMovieId(), sysUser.getId())))
                return Utils.resFailure(400, "你已收藏过该电影！");
            favorite.setUserId(sysUser.getId());
            favorite.setCreateTime(new Date());
            movieMapper.createMovieFavorite(favorite);
            return Utils.resSuccess(200, "提交成功！", null);
        } catch (Exception e) {
            return Utils.resFailure(400, "未知错误,提交失败!");
        }
    }

    /**
     * 查询是否已收藏
     */
    @Override
    public DataGridView ifMovieFavorite(Integer movieId) {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            Favorite movieFavorite = movieMapper.getMovieFavorite(movieId, sysUser.getId());
            movieFavorite.setMovieId(null);
            movieFavorite.setUserId(null);
            return Utils.resSuccess(200, "", movieFavorite);
        } catch (Exception e) {
            return Utils.resFailure(400, "未知错误,提交失败!");
        }
    }

    /**
     * 删除已收藏
     */
    @Override
    public DataGridView delMovieFavorite(Favorite favorite) {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            movieMapper.delMovieFavorite(favorite.getMovieId(), sysUser.getId());
            return Utils.resSuccess(200, "提交成功！", null);
        } catch (Exception e) {
            return Utils.resFailure(400, "未知错误,提交失败!");
        }
    }

    @Override
    public DataGridView hotList(NewsVo newsVo) {
        String sortList = "-create_time, -news_view";
        if(!Objects.isNull(newsVo.getNewsSort())){
            if (newsVo.getNewsSort() == 1) sortList = "-news_view, -create_time";
        }
        try {
            Page<Object> page = PageHelper.startPage(newsVo.getPage(), newsVo.getSize(), sortList);
            List<News> newsList = movieMapper.hotList(newsVo);
            return Utils.pagSuccess(200, "", page.getTotal(), newsList);
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
    }

    @Override
    public DataGridView videoCallMovies(Integer cinemaId) {
        try {
            List<MoviesDetailVo> moviesDetailVos = movieMapper.videoCallMovies(cinemaId);
            return Utils.resSuccess(200, "", moviesDetailVos);
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
    }

    @Override
    public DataGridView userCollectMovies(MoviesPageVo moviesPageVo) {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            Page<Object> page = PageHelper.startPage(moviesPageVo.getPage(),moviesPageVo.getSize(), "-f.create_time");
            List<Movies> userMovieFavorites = movieMapper.userCollectMovie(sysUser.getId());
            return Utils.pagSuccess(200, "", page.getTotal(), userMovieFavorites);
        } catch (Exception e) {
            return Utils.resFailure(400, "未知错误,提交失败!");
        }
    }

    /**
     * 用户评论的电影列表
     * @return
     */
    @Override
    public DataGridView userCommentMovies(CommentVo commentVo) {
        try {
            RedisUser redisUser = UserUtils.getLoginUser();
            SysUser sysUser = redisUser.getSysUser();
            Page<Object> page = PageHelper.startPage(commentVo.getPage(),commentVo.getSize(), "-c.update_time");
            List<CommentDetailVo> userCommentMovies = commentMapper.userCommentMovie(sysUser.getId());
            return Utils.pagSuccess(200, "", page.getTotal(), userCommentMovies);
        } catch (Exception e) {
            return Utils.resFailure(400, "未知错误,提交失败!");
        }
    }
}
