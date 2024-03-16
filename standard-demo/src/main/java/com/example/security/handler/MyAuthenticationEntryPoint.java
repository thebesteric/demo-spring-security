package com.example.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 登录失败处理逻辑
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-05 18:33:56
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = authException.getMessage();
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<h3 style='color: blue'>登录失败：" + message + "</h3>");
        response.getWriter().println("<a href='/login'>返回登录</a>");
        response.getWriter().flush();
    }
}
