package com.movie.domain.vo;

import com.movie.domain.entity.Movies;
import io.swagger.annotations.ApiParam;

public class MoviesPageVo extends Movies {
    @ApiParam("页码")
    private Integer page = 1;
    @ApiParam("一页数据")
    private Integer size = 10;
    @ApiParam("排序字段sort 0:按热门排序 /1：按时间排序/2：按评价排序")
    private Integer movieSort = 0;

    @ApiParam("搜索关键字")
    private String keyWord;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getMovieSort() {
        return movieSort;
    }

    public void setMovieSort(Integer movieSort) {
        this.movieSort = movieSort;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
