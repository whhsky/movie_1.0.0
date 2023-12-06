package com.movie.cinema.service.impl;

import com.cinema.domain.entity.CinemaAndMovies;
import com.cinema.domain.entity.Seat;
import com.cinema.domain.vo.CinemaAndMoviesDetail;
import com.cinema.domain.vo.CinemaTagsVo;
import com.cinema.domain.vo.CinemaVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.movie.cinema.mapper.CinemaMapper;
import com.movie.cinema.service.ICinemaService;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ICinemaServiceImpl implements ICinemaService {

    @Autowired
    private CinemaMapper cinemaMapper;

    /**
     * 影院列表
     * 排序字段cinemaSort 0: 按距离排序 / 1：按价格排序
     * @param cinemaVo
     * @return
     */
    @Override
    public DataGridView cinemaList(CinemaVo cinemaVo) {
        String sort;
        if(cinemaVo.getCinemaSort() == 0){
            sort = "cinema_jl";
        }else if (cinemaVo.getCinemaSort() == 1){
            sort = "cinema_avg_price";
        }else {
            sort = null;
        }

        try {
            Page<Object> page = PageHelper.startPage(cinemaVo.getPage(), cinemaVo.getSize(), sort);
            List<CinemaTagsVo> cinemas = cinemaMapper.cinemaList(cinemaVo);
            for (CinemaTagsVo cinema : cinemas) {
                List<String> tags = Arrays.asList(
                        cinemaMapper.cinemaTags(cinema.getCinemaType().split(",")
                        ).split(",")
                );
                cinema.setCinemaTags(tags);
            }
            return Utils.pagSuccess(200, "", page.getTotal(), cinemas);
        }catch (Exception e){
            return Utils.resFailure(400, "");
        }
    }

    /**
     * 放映详情
     * @param cinemaAndMovies
     * @return
     */
    @Override
    public DataGridView videoCall(CinemaAndMovies cinemaAndMovies) {
        try {
            List<CinemaAndMovies> movieVideoList = cinemaMapper.videoCall(cinemaAndMovies);
            int movieVideSize = movieVideoList.size();
            if( movieVideSize > 0 ) {
                Map<LocalDate, List<CinemaAndMovies>> CinemaAndMoviesMap = new HashMap<>();
                for (CinemaAndMovies andMovies : movieVideoList) {
                    LocalDate date = andMovies.getViewStartTime().toLocalDate();
                    CinemaAndMoviesMap.computeIfAbsent(date, k -> new ArrayList<>()).add(andMovies);
                }
                return Utils.pagSuccess(200, "", (long) movieVideoList.size(),CinemaAndMoviesMap);
            }
            return Utils.pagSuccess(200, "", 0L, null);
        }catch (Exception e){
            return Utils.resFailure(400, "");
        }
    }

    /**
     * 返回座位信息以及目标电影信息
     * 座位信息： 0 / 1 / 2 分表表示: 0:不可用 / 1：座位可选 / 2：座位已售
     * @param viewId
     * @return
     */
    @Override
    public DataGridView videoSeatView(Long viewId) {
        HashMap<String, Object> resData;
        try {
            // 获取上映电影影厅信息
            CinemaAndMoviesDetail view = cinemaMapper.videoView(viewId);
            if (view == null){
                return Utils.resFailure(400, "未收录！！！");
            }
            Integer videoRow = view.getVideoRow();
            Integer videoColumn = view.getVideoColumn();
            List<List<Integer>> seatList = new ArrayList<>();
            for (int i = 0; i < videoRow; i++) {
                List<Integer> seat = new ArrayList<>();
                for (int j = 0; j < videoColumn; j++) {
                    seat.add(1);
                }
                seatList.add(seat);
            }

            // 获取不可用座位
            List<Seat> seatDetail0  = cinemaMapper.getSeatDetail(0, view.getVideoCallId(), view.getCinemaId(), view.getId());
            if (seatDetail0.size() > 0){
                for (Seat seat : seatDetail0) {
                        // 设置不可用座位
                        seatList.get(seat.getSeatRow()).set(seat.getSeatColumn(), 0);
                }
            }
            // 获取已售座位
            List<Seat> seatDetail2  = cinemaMapper.getSeatDetail(2, view.getVideoCallId(), view.getCinemaId(), view.getId());
            if (seatDetail2.size() > 0){
                for (Seat seat : seatDetail2) {
                    // 设置已售座位
                    seatList.get(seat.getSeatRow()).set(seat.getSeatColumn(), 2);
                }
            }
            resData = new HashMap<>();
            resData.put("seat", seatList);
            resData.put("view_info", view);
            return Utils.resSuccess(200, "", resData);
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
    }

}
