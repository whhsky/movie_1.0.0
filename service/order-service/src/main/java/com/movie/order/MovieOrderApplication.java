package com.movie.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.movie.user.client", "com.movie.cinema.client", "com.movie.movie.client"})
@SpringBootApplication(scanBasePackages = {"com.movie.utils", "com.movie.order"})
public class MovieOrderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MovieOrderApplication.class, args);
        log.info("项目启动成功！");
    }
}
