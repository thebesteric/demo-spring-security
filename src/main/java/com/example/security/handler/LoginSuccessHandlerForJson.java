package com.example.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证成功后，代码处理逻辑
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-03 23:18:16
 */
public class LoginSuccessHandlerForJson implements AuthenticationSuccessHandler {

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
        Object principal = authentication.getPrincipal();
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(principal));
        out.flush();
        out.close();
    }
}
