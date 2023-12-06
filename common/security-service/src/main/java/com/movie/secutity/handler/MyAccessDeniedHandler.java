package com.movie.secutity.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler, AuthenticationEntryPoint{
    private static final Log logger = LogFactory.getLog(MyAccessDeniedHandler.class);

    /**
     * 自定义认证异常处理
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String msg = "Full authentication is required to access this resource".equals(authException.getMessage()) ? "访问受限，授权过期": authException.getMessage();
        DataGridView dataGridView = Utils.resFailure(403, msg);
        logger.debug(msg);
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(dataGridView));
        writer.flush();
        writer.close();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        DataGridView dataGridView = Utils.resFailure(403, accessDeniedException.getMessage());
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(dataGridView));
        writer.flush();
        writer.close();
    }

}



