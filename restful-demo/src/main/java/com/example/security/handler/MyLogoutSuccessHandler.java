package com.example.security.handler;

import com.example.domain.R;
import com.example.util.SecurityUtils;
import com.example.util.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登出成功处理器
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-08 11:13:02
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1、从请求中获取 token
        String token = request.getHeader("x-token");
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationServiceException("用户未认证");
        }
        // 2、删除 token
        if (Boolean.TRUE.equals(redisTemplate.delete(token))) {
            // 3、清空上下文
            SecurityUtils.clearContext();
            // 4、返回
            ServletUtils.renderString(response, R.success());
            return;
        }
        throw new AuthenticationServiceException("用户未认证");
    }
}
