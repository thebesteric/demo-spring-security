package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Map;

/**
 * 请求未认证处理
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-09 17:42:08
 */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = authException.getLocalizedMessage();
        Map<String, Object> result = Map.of(
                "code", 401,
                "message", message
        );
        // 返回 Json 格式数据
        ServletUtils.renderString(response, result);
    }
}
