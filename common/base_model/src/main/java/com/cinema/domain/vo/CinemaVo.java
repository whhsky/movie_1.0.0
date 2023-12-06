package com.cinema.domain.vo;

import com.cinema.domain.entity.Cinema;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class CinemaVo extends Cinema {
    @ApiParam("页码")
    private Integer page = 1;
    @ApiParam("一页数据")
    private Integer size = 10;
    @ApiParam("排序字段sort 0: 按距离排序 / 1：按价格排序")
    private Integer cinemaSort = 1;
    @ApiParam("搜索关键字")
    private String keyWord;
    @ApiParam("电影id(关联电影id)")
    private Integer MovieId;
}
