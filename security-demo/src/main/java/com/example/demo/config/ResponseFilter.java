package com.example.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException; /**
 * ResponseFilter
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-23 16:25:08
 */
@Component
public class ResponseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}