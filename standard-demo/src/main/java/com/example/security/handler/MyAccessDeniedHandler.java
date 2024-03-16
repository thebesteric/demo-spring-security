package com.example.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 没有权限访问逻辑
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-05 18:15:58
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 处理 403 的方式
     *
     * @param request               HttpServletRequest
     * @param response              HttpServletResponse
     * @param accessDeniedException 异常对象
     * @author wangweijun
     * @since 2023/12/5 18:16
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = accessDeniedException.getMessage();
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<h3 style='color: blue'>权限不足，请联系系统管理员：" + message + "</h3>");
        response.getWriter().println("<a href='/main'>返回首页</a>");
        response.getWriter().flush();
    }
}
