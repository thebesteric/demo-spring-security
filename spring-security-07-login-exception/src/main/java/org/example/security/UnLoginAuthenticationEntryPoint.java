package org.example.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * AuthenticationEntryPoint
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-30 19:23:34
 */
public class UnLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("您未登录，请先登录！");
        out.flush();
        out.close();
    }
}
