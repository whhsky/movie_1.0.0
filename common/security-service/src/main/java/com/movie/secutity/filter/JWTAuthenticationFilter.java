package com.movie.secutity.filter;

import com.movie.secutity.domain.RedisUser;
import com.movie.secutity.handler.MyAccessDeniedHandler;
import com.movie.user.client.UserFeignClient;
import com.movie.utils.JWTUtils;
import com.movie.utils.RedisUtils;
import com.user.domain.entity.SysUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

/**
 * 自定义jwt权限认证过滤器
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MyAccessDeniedHandler authenticationEntryPoint;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 获取请求token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 验证token信息
        Claims claims = JWTUtils.verifyJwt(token);
        if (!Objects.isNull(claims)) {
            try {
                // 从redis中验证token信息是否存在
                String userId = String.valueOf(claims.get("userId"));
                if (!token.equals(redisUtils.get("userid:" + userId + "Token")) || Objects.isNull(redisUtils.get("userid:" + userId + "Token"))) {
                    authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("认证信息已过期！"));
                    return;
                }
                // 从redis中获取用户信息
                RedisUser userDetails = (RedisUser) redisUtils.get("userid:" + userId);
                if (Objects.isNull(userDetails)) {  // 如果获取不到再去数据库中查询用户信息
                    // 查询用户信息
                    SysUser resultSysUser = userFeignClient.userInfo(String.valueOf(claims.get("sub")));
                    userDetails = new RedisUser(resultSysUser, new HashSet<>());
                    // 将用户信息在放到redis中去
                    redisUtils.set("userid:" + resultSysUser.getId().toString(), userDetails);
                }

                // authenticate 进行用户认证
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContextHolder.setContext(context);
            }catch (Exception e) {
                authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("500: 服务器内部错误！"));
            }
            // 请求放行
            filterChain.doFilter(request, response);
        }else {
            authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("token非法！"));
        }
    }
}
