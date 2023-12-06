package com.movie.domain.dto;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MovieDto {
    @Async
    public void hello(){
        System.out.println("qq!");
    }
}
