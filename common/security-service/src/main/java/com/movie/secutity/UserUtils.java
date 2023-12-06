package com.movie.secutity;

import com.movie.secutity.domain.RedisUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    public static RedisUser getLoginUser() {
        return (RedisUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
