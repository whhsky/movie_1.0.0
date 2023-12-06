package com.movie.sysdict.client;


import com.sysdict.domain.entity.SysDictData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "sysdict-service")
public interface SysdictFeignClient {
    @GetMapping("/app/sys/inner/Sys_dict_data/{status}")
    SysDictData SysDictData(@PathVariable("status") Integer Status);
}
