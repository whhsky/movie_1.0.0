package com.movie.cast.controller;

import com.movie.cast.service.ICastService;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import com.movie.utils.argumentResolverConfig.ParameterConvert;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation("演职员人控制类")
@RequestMapping("/app/cast")
@RestController
public class CastController {

    @Autowired
    private ICastService ICastService;

    @ApiOperation("根据电影id获取该电影演职人员")
    @GetMapping("/movie_cast")
    @ParameterConvert
    public DataGridView movieCast(@ApiParam(name = "movie_id", value = "电影id", required = true) Integer  movieId){
        if(movieId == null) return Utils.resFailure(400, "");
        return ICastService.getMovieCast(movieId);

    }
}
