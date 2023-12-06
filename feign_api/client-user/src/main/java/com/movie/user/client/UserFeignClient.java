package com.movie.user.client;

import com.user.domain.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "user-service")
public interface UserFeignClient {
    @GetMapping("/app/user/inner/user_info/{user_name}")
    SysUser userInfo(@PathVariable("user_name") String username);

    @GetMapping("/app/user/inner/purse/{user_id}")
    Double iPurse(@PathVariable("user_id") Integer userId);

    @PutMapping("/app/user/inner/set_purse/{overage}/{id}")
    void setUserOverage(@PathVariable("overage") Double overage, @PathVariable("id") Integer id);
}
