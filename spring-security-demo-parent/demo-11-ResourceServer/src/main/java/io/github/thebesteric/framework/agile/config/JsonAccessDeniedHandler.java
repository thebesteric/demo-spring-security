package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Map;

/**
 * 权限访问拒绝
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-12 11:15:28
 */
public class JsonAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = accessDeniedException.getLocalizedMessage();
        Map<String, Object> result = Map.of(
                "code", 403,
                "message", message
        );
        // 返回 Json 格式数据
        ServletUtils.renderString(response, result);
    }
}
