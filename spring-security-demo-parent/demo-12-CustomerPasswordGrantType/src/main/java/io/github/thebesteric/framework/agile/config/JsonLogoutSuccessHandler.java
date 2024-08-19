package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.Map;

/**
 * 注销成功处理
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-09 17:31:07
 */
public class JsonLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> result = Map.of(
                "code", 200,
                "message", "注销成功"
        );
        // 返回 Json 格式数据
        ServletUtils.renderString(response, result);
    }
}
