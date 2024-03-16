package org.example.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

/**
 * LoginSuccessHandler
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 13:49:09
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 凭证
        Object credentials = authentication.getCredentials();
        // 身份
        Object principal = authentication.getPrincipal();
        // 权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(String.format("Login OK: [登陆凭证：%s, 身份：%s, 权限: %s]", credentials, principal, authorities));
    }
}
