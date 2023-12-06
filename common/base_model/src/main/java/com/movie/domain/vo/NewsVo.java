package com.movie.domain.vo;

import com.movie.domain.entity.News;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class NewsVo extends News {
    @ApiParam("页码")
    private Integer page = 1;
    @ApiParam("一页数据")
    private Integer size = 10;
    @ApiParam("排序字段sort 0:按时间排序 / 1：按热度排序 ")
    private Integer newsSort = 0;
    @ApiParam("搜索关键字")
    private String keyWord;
}
