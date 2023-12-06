package com.cinema.domain.vo;

import com.cinema.domain.entity.Cinema;
import lombok.Data;

import java.util.List;

@Data
public class CinemaTagsVo extends Cinema {
    private List<String> cinemaTags;
}
