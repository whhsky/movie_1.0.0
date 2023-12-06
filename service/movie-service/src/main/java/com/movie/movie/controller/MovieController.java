package com.movie.movie.controller;


import com.movie.domain.entity.Comment;
import com.movie.domain.entity.Favorite;
import com.movie.domain.vo.CommentVo;
import com.movie.domain.vo.MoviesDetailVo;
import com.movie.domain.vo.MoviesPageVo;
import com.movie.domain.vo.NewsVo;
import com.movie.movie.mapper.MovieMapper;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import com.movie.utils.argumentResolverConfig.ParameterConvert;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.movie.movie.service.IMovieService;

@ApiOperation("电影控制类")
@RequestMapping("/app/movies")
@RestController
public class MovieController {

    @Autowired
    private IMovieService IMovieService;

    @Autowired
    private MovieMapper movieMapper;

    @ApiOperation("主页轮播图接口")
    @GetMapping("/banner")
    @ParameterConvert
    public DataGridView Banner(
            @ApiParam(name = "banner_code", value = "轮播类型代号首页45,新闻页46", required = true)
            Integer bannerCode){
        if(bannerCode == null) return Utils.resFailure(400, "");
        return IMovieService.getBanner(bannerCode);
    }

    @GetMapping("/movie_images")
    @ParameterConvert
    public DataGridView movieImages(
            @ApiParam(name = "movie_id", value = "电影id", required = true)
            Integer movieId){
        if(movieId == null) return Utils.resFailure(400, "");
        return IMovieService.getMovieImages(movieId);
    }

    @ApiOperation("电影信息接口")
    @GetMapping("/movie_list")
    @ParameterConvert
    public DataGridView movieList(MoviesPageVo moviesPageVo){
        return IMovieService.movieList(moviesPageVo);
    }

    @ApiOperation("首页排行榜")
    @GetMapping("/home_rank_list")
    public DataGridView rankList(){
        return IMovieService.rankList();
    }

    @ApiOperation("热映电影票房榜单")
    @GetMapping("/showing_list")
    @ParameterConvert
    public DataGridView showingList(@ApiParam(required = true, value = "0 热映口碑榜： 1 票房榜 2 期待人数榜")
                                        @Range(min = 0, max = 2,message = "范围在0-2之间") String selectType,
                                    @ApiParam(required = false, value = "地区")  String movieRegion){
        if(selectType == null) {return Utils.resFailure(400, "");}
        return IMovieService.movieRegionRank(selectType, movieRegion);
    }

    @ApiOperation("TOP100榜 按票房排序")
    @GetMapping("/top100_list")
        @ParameterConvert
        public DataGridView top100(@ApiParam("第几条开始") String page,  @ApiParam("条数") String size){
            return IMovieService.top100(page, size);
    }

    @ApiOperation("电影详细信息")
    @GetMapping("/movie_detail/{id}")
    @ParameterConvert
    public DataGridView movieDetail(@PathVariable("id") Integer id){
        if(id == null) return Utils.resFailure(400, "");
        return IMovieService.movieDetail(id);
    }

    @ApiOperation("获取电影影评信息")
    @GetMapping("/comment")
    @ParameterConvert
    public DataGridView getMovieComment(CommentVo commentVo){
        return IMovieService.getMovieComment(commentVo);
    }

    @ApiOperation("提交电影影评信息")
    @PostMapping("/comment")
    @ParameterConvert
    public DataGridView movieComment(@RequestBody Comment comment){
        if(comment.getContent() == null ||  "".equals(comment.getContent())) return Utils.resFailure(400, "");
        return IMovieService.movieComment(comment);
    }

//    @ApiOperation("删除电影评论")
//    @DeleteMapping("/comment")
//    @ParameterConvert
//    public DataGridView delMovieComment(@RequestBody Comment comment){
//        if (comment.getMovieId() == null) return Utils.resFailure(400, "电影未知!");
//        return movieService.delMovieComment(comment);
//    }

    @ApiOperation("查询用户收藏电列表")
    @GetMapping("/collect")
    @ParameterConvert
    public DataGridView userCollectMovie(MoviesPageVo moviesPageVo){
        return IMovieService.userCollectMovies(moviesPageVo);
    }

    @ApiOperation("查询用户评论电影列表")
    @GetMapping("/user_comment")
    @ParameterConvert
    public DataGridView userCommentMovies(CommentVo commentVo){
        return IMovieService.userCommentMovies(commentVo);
    }

    @ApiOperation("收藏电影")
    @PostMapping("/favorite")
    @ParameterConvert
    public DataGridView createMovieFavorite(@RequestBody Favorite favorite){
        if (favorite.getMovieId() == null) return Utils.resFailure(400, "电影未知!");
        return IMovieService.createMovieFavorite(favorite);
    }

    @ApiOperation("查询某电影是否已收藏")
    @GetMapping("/favorite")
    @ParameterConvert
    public DataGridView ifMovieFavorite(Integer movieId){
        if (movieId == null) return Utils.resFailure(400, "电影未知!");
        return IMovieService.ifMovieFavorite(movieId);
    }

    @ApiOperation("删除收藏电影")
    @DeleteMapping("/favorite")
    @ParameterConvert
    public DataGridView delMovieFavorite(@RequestBody Favorite favorite){
        if (favorite.getMovieId() == null) return Utils.resFailure(400, "电影未知!");
        return IMovieService.delMovieFavorite(favorite);
    }

    @ApiOperation("热点资讯")
    @GetMapping("/hot_list")
    @ParameterConvert
    public DataGridView hotList(NewsVo newsVo){
        return IMovieService.hotList(newsVo);
    }

    @ApiOperation("获取影院上映电影信息")
    @GetMapping("/video_call_movie_list")
    @ParameterConvert
    public DataGridView videoCallMovieList(Integer cinemaId){
        return IMovieService.videoCallMovies(cinemaId);
    }

    /**
     * 获取电影详情 服务间远程调用
     */
    @GetMapping("/inner/movie/{id}")
    public MoviesDetailVo getMovieDetail(@PathVariable("id") Integer movieId){
        return movieMapper.getMovieDetailById(movieId);
    }


}
