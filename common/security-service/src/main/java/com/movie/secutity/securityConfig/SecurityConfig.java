package com.movie.secutity.securityConfig;

import com.movie.secutity.filter.JWTAuthenticationFilter;
import com.movie.secutity.handler.MyAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /**
         * .authorizeRequests：表示验证请求
         * .antMatchers(String... antPatterns)：使用 {@link AntPathRequestMatcher} 的匹配规则
         * .antMatchers("/") : 表示应用的首页
         * .permitAll()：表示允许一切用户访问，底层调用 access("permitAll")
         * .hasRole(String role)：表示 antMatchers 中的 url 请求允许此角色访问
         * .hasAnyRole(String... roles) : 表示 antMatchers 中的 url 请求允许这多个角色访问
         * .access(String attribute)：表示允许访问的角色，permitAll、hasRole、hasAnyRole 底层都是调用 access 方法
         * .access("permitAll") 等价于 permitAll()
         * "/**" 表示匹配任意
         */
        http.csrf().disable();
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/app/user/login/").permitAll()
                .antMatchers("/**/inner/**").permitAll()
                .antMatchers("/app/user/register/").permitAll()
                .antMatchers("/app/user/**").authenticated()
                .antMatchers("/app/order/**").authenticated()
                .antMatchers(HttpMethod.POST, "/app/movies/comment/").authenticated()
                .antMatchers(HttpMethod.GET, "/app/movies/favorite/").authenticated()
                .antMatchers(HttpMethod.POST, "/app/movies/favorite/").authenticated()
                .antMatchers(HttpMethod.DELETE, "/app/movies/favorite/").authenticated()
                .antMatchers(HttpMethod.GET, "/upload/**").permitAll()
                .antMatchers("/app/file/**").permitAll()
                .antMatchers("/app/cinema/**").permitAll()
                .antMatchers("/app/cast/**").permitAll()
                .antMatchers("/app/sys/**").permitAll()
                .antMatchers(HttpMethod.GET, "/app/movies/**").permitAll()
                .antMatchers("/static/**", "/favicon.ico","/webjars/**", "/swagger-resources/**", "/v2/**", "/error/**").permitAll()
//                        .antMatchers("/static/**", "/favicon.ico","/webjars/**", "/doc.html",
//                                     "/swagger-resources/**", "/v2/**", "/error/**").permitAll()
                .anyRequest().denyAll();
        http.exceptionHandling().authenticationEntryPoint(myAccessDeniedHandler);
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors();
    }
}
