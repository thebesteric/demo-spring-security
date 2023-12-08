package com.example.security.handler;

import com.example.domain.R;
import com.example.util.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 没有权限访问逻辑
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-05 18:15:58
 */
@Slf4j
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 处理 403 的方式
     *
     * @param request               HttpServletRequest
     * @param response              HttpServletResponse
     * @param accessDeniedException 异常对象
     * @author wangweijun
     * @since 2023/12/5 18:16
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("[权限异常]: {}", accessDeniedException.getMessage(), accessDeniedException);
        String message = accessDeniedException.getMessage();
        ServletUtils.renderString(response, R.error(403, "[权限异常]: " + message));
    }
}
