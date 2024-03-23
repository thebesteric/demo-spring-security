package com.example.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * AuthenticationSuccessHandler 登录成功处理器
 * AuthenticationFailureHandler 登录失败处理器
 * AccessDeniedHandler 权限访问拒绝处理器
 * LogoutSuccessHandler 退出成功处理器
 * LogoutHandler 退出时执行处理器
 */
@Component
public class SpringSecurityHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler
        , AccessDeniedHandler, LogoutSuccessHandler, LogoutHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        writer.println("登录失败");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        writer.println("登录成功");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        writer.println("访问被拒绝");
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.println("正在退出账号");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrintWriter writer = response.getWriter();
        writer.println("退出账号成功");
    }

}
