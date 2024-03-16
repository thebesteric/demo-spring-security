package com.example.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * LoginFailHandler
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-04 00:01:00
 */
public class LoginFailHandler implements AuthenticationFailureHandler {
    // 登录成功后的访问地址
    private String url;

    // 是否是重定向
    private boolean redirect;

    public LoginFailHandler(String url, boolean redirect) {
        this.url = url;
        this.redirect = redirect;
    }

    /**
     * 认证失败后的具体处理逻辑
     *
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param exception 异常信息
     * @author wangweijun
     * @since 2023/12/4 00:02
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (redirect) {
            // 重定向
            response.sendRedirect(url);
        } else {
            // 转发
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
}
