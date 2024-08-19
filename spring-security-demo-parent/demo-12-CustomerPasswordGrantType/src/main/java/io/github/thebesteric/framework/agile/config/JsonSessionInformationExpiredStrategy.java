package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.util.Map;

/**
 * 会话并发处理策略
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-09 18:26:01
 */
public class JsonSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        Map<String, Object> result = Map.of(
                "code", 401,
                "message", "账号已从其他设备登录"
        );
        // 返回 Json 格式数据
        ServletUtils.renderString(response, result);
    }
}
