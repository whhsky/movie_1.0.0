package com.movie.cinema.mapper;

import com.cinema.domain.entity.CinemaAndMovies;
import com.cinema.domain.entity.Seat;
import com.cinema.domain.vo.CinemaAndMoviesDetail;
import com.cinema.domain.vo.CinemaTagsVo;
import com.cinema.domain.vo.CinemaVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CinemaMapper {
    // 获取影院
    List<CinemaTagsVo> cinemaList(CinemaVo cinemaVo);

    // 获取标签
    String cinemaTags(String[] cinemaTypes);

    // 根据影院获取放映室列表
    List<CinemaAndMovies> videoCall(CinemaAndMovies cinemaAndMovies);

    // 获取一个电影放映室详情
    CinemaAndMoviesDetail videoView(Long viewId);

    // 获取座位详情
    List<Seat> getSeatDetail(@Param("seatStatus") Integer seatStatus, @Param("videoCallId")Integer videoCallId, @Param("cinemaId")Integer cinemaId, @Param("mcId")Long mcId);

    // 查询座位信息
    Seat SeatDetail2(@Param("seatRow") Integer seatRow, @Param("seatColumn") Integer seatColumn, @Param("cinemaId") Integer cinemaId, @Param("videoCallId") Integer videoCallId, @Param("mcId")Long mcId);

    // 创建座位
    void createSeat(@Param("seatRow") Integer seatRow, @Param("seatColumn") Integer seatColumn, @Param("cinemaId") Integer cinemaId, @Param("videoCallId") Integer videoCallId, @Param("userId")Long userId);

    // 锁定座位
    void createSeat(Seat seatInfo);

    // 消除座位
    void deleteSeats(String id);
}
