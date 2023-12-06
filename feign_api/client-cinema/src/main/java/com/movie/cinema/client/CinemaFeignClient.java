package com.movie.cinema.client;


import com.cinema.domain.entity.Seat;
import com.cinema.domain.vo.CinemaAndMoviesDetail;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "cinema-service")
public interface CinemaFeignClient {
    @GetMapping("/app/cinema/inner/delete_seats/{seat_id}")
    void deleteSeats(@PathVariable("seat_id") String seatId);

    @GetMapping("/app/cinema/inner/get_video_view/{view_id}")
    CinemaAndMoviesDetail getVideoView(@PathVariable("view_id") Long view_id);

    @GetMapping("/app/cinema/inner/seat_detail2/{seat_row}/{seat_column}/{cinema_id}/{video_call_id}/{mc_id}")
    Seat getSeatDetail2(@PathVariable("seat_row") Integer seatRow,
                        @PathVariable("seat_column") Integer seatColumn,
                        @PathVariable("cinema_id") Integer cinemaId,
                        @PathVariable("video_call_id") Integer videoCallId,
                        @PathVariable("mc_id") Long mcId);

    @PostMapping("/app/cinema/inner/createSeat")
    Long createSeat(@RequestBody Seat seatInfo);
}
