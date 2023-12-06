package com.movie.movie.mapper;


import com.movie.domain.entity.Comment;
import com.movie.domain.vo.CommentDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentMapper {
    List<CommentDetailVo> getMovieComment(Integer movieId);
    List<Comment> ifGetMovieComment(@Param("movieId") Integer movieId, @Param("userId")Integer userId, @Param("replyId")Integer replyId);
    void createComment(Comment comment);

    List<CommentDetailVo> userCommentMovie(Integer id);
}
