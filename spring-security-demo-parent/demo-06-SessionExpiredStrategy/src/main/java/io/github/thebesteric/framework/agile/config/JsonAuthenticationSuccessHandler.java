package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * 认证成功后的处理
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-09 16:58:47
 */
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 用户身份信息
        Object principal = authentication.getPrincipal();
        // 用户凭证信息
        Object credentials = authentication.getCredentials();
        // 用户权限信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Map<String, Object> result = Map.of(
                "code", 200,
                "message", "登录成功",
                "data", principal
        );

        // 返回 Json 格式数据
        ServletUtils.renderString(response, result);
    }
}
