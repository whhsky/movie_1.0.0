package com.movie.cinema.controller;

import com.cinema.domain.entity.CinemaAndMovies;
import com.cinema.domain.entity.Seat;
import com.cinema.domain.vo.CinemaAndMoviesDetail;
import com.cinema.domain.vo.CinemaVo;
import com.movie.cinema.mapper.CinemaMapper;
import com.movie.cinema.service.ICinemaService;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import com.movie.utils.argumentResolverConfig.ParameterConvert;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@ApiOperation("影院控制类")
@RestController
@RequestMapping("/app/cinema")
public class CinemaController {
    @Autowired
    private ICinemaService cinemaService;

    @Autowired
    private CinemaMapper cinemaMapper;

    @ApiOperation("影院信息")
    @GetMapping("/cinema_list")
    @ParameterConvert
    public DataGridView cinemaList(CinemaVo cinemaVo){
        if (cinemaVo.getCinemaType() != null){
            String[] types = cinemaVo.getCinemaType().split(",");
            cinemaVo.setCinemaType(types[0]);
        }
        return cinemaService.cinemaList(cinemaVo);
    }

    @ApiOperation("电影放映信息")
    @GetMapping("/video_call")
    @ParameterConvert
    public DataGridView videoCall(CinemaAndMovies cinemaAndMovies){
        if (cinemaAndMovies.getMovieId() == null || cinemaAndMovies.getCinemaId() == null){
            return Utils.resFailure(400, "");
        }
        return cinemaService.videoCall(cinemaAndMovies);
    }

    @ApiOperation("获取影厅电影座位")
    @GetMapping("/video_seat_view")
    @ParameterConvert
    public DataGridView videoSeatView(String viewId){
        if (Objects.isNull(viewId)){
            return Utils.resFailure(400, "");
        }
        return cinemaService.videoSeatView(Long.valueOf(viewId));
    }

    /**
     * 清楚座位状态-服务接口间调用
     * @param seatId
     */
    @GetMapping("/inner/delete_seats/{seat_id}")
    public void deleteSeats(@PathVariable("seat_id") String seatId){
        try {
            cinemaMapper.deleteSeats(seatId);
        } catch (Exception e) {
            log.debug("删除失败，出现异常！！！{}", e.getMessage());
            throw new RuntimeException("删除失败，出现异常！！！", e);
        }
    }

    /**
     * 获取放映射详情 服务间接口调用
     * @return CinemaAndMoviesDetail
     */
    @GetMapping("/inner/get_video_view/{view_id}")
    public CinemaAndMoviesDetail getVideoView(@PathVariable("view_id") Long viewId){
        return cinemaMapper.videoView(viewId);
    }

    /**
     * 查询座位信息 服务间接口调用
     * @return CinemaAndMoviesDetail
     */
    @GetMapping("/inner/seat_detail2/{seat_row}/{seat_column}/{cinema_id}/{video_call_id}/{mc_id}")
    public Seat getSeatDetail2(@PathVariable("seat_row") Integer seatRow,
                               @PathVariable("seat_column") Integer seatColumn,
                               @PathVariable("cinema_id") Integer cinemaId,
                               @PathVariable("video_call_id") Integer videoCallId,
                               @PathVariable("mc_id") Long mcId){
        return cinemaMapper.SeatDetail2(seatRow, seatColumn, cinemaId, videoCallId, mcId);
    }

    /**
     * 创建座位 服务间接口调用
     */
    @PostMapping("/inner/createSeat")
    public Long createSeat(@RequestBody Seat seatInfo){
        try {
            cinemaMapper.createSeat(seatInfo);
            return seatInfo.getId();
        } catch (Exception e) {
            log.debug("创建失败，出现异常！！！{}", e.getMessage());
            throw new RuntimeException("创建失败，出现异常！！！", e);
        }
    }
}
