package com.example.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * LogoutSuccessHandler
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-04 18:59:21
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    /**
     * 退出登录后的业务处理
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param authentication 用户主体对象
     * @author wangweijun
     * @since 2023/12/4 19:00
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("退出登录后的一些逻辑处理...");
        // 销毁对象，清空缓存
        request.getSession().invalidate();
        // 设置为未登录状态
        authentication.setAuthenticated(false);
        // 重定向
        response.sendRedirect("/login?logout");
    }
}
