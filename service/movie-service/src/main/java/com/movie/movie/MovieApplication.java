package com.movie.movie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.movie.user.client","com.movie.sysdict.client"})
@SpringBootApplication(scanBasePackages = {"com.movie.utils", "com.movie.movie"})
public class MovieApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MovieApplication.class, args);
        log.info("项目启动成功！");
    }
}
