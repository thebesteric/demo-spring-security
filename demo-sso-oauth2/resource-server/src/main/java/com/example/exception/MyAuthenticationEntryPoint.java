package com.example.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;

/**
 * 自定义 AuthenticationEntryPoint
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof InsufficientAuthenticationException) {
            String accept = request.getHeader("accept");
            if (accept.contains(MediaType.TEXT_HTML_VALUE)) {
                // 如果是html请求类型，则返回登录页
                LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
                loginUrlAuthenticationEntryPoint.commence(request, response, authException);
            } else {
                // 如果是api请求类型，则返回 json
                ResponseResult.exceptionResponse(response, "需要带上令牌进行访问");
            }
        } else if (authException instanceof InvalidBearerTokenException) {
            ResponseResult.exceptionResponse(response, "令牌无效或已过期");
        } else {
            ResponseResult.exceptionResponse(response, authException);
        }
    }
}
