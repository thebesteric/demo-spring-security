package com.example.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 认证成功后，代码处理逻辑
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-03 23:18:16
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    // 登录成功后的访问地址
    private String url;

    // 是否是重定向
    private boolean redirect;

    public LoginSuccessHandler(String url, boolean redirect) {
        this.url = url;
        this.redirect = redirect;
    }

    /**
     * 认证成功后的具体处理逻辑
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param authentication 认证成功后的用户主体对象，包含用户的个人信息和权限列表
     * @author wangweijun
     * @since 2023/12/3 23:18
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (redirect) {
            // 重定向
            response.sendRedirect(url);
        } else {
            // 转发
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
}
