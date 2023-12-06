package com.movie.domain.vo;


import com.movie.domain.entity.Comment;
import io.swagger.annotations.ApiParam;

public class CommentVo extends Comment {
    @ApiParam("页码")
    private Integer page = 1;
    @ApiParam("一页数据")
    private Integer size = 10;
    @ApiParam("排序字段sort 0:按热门排序 /1：按时间排序")
    private Integer commentSort = 0;

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

    public Integer getCommentSort() {
        return commentSort;
    }

    public void setCommentSort(Integer commentSort) {
        this.commentSort = commentSort;
    }
}
