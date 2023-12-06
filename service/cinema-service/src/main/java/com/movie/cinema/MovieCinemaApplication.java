package com.movie.cinema;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.movie.user.client"})
@SpringBootApplication(scanBasePackages = {"com.movie.utils", "com.movie.cinema"})
public class MovieCinemaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MovieCinemaApplication.class, args);
        log.info("项目启动成功！");
    }
}
