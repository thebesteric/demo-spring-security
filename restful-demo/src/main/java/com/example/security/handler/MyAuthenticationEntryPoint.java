package com.example.security.handler;

import com.example.domain.R;
import com.example.util.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登录失败处理逻辑
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-05 18:33:56
 */
@Slf4j
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("[认证异常]: {}", authException.getMessage(), authException);
        String message = authException.getMessage();
        ServletUtils.renderString(response, R.error(401, "[认证异常]: " + message));
    }
}
