package com.example.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * MyLogoutHandler
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-04 19:12:44
 */
public class MyLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 一般做额外处理，比如保存某些会话中的属性到数据库
        System.out.println("退出登录后的一些额外逻辑处理...");
    }
}
