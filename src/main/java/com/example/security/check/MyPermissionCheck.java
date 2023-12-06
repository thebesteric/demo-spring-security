package com.example.security.check;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface MyPermissionCheck {

    /**
     * 自定义权限校验方法
     *
     * @param request HttpServletRequest
     * @param authentication 认证用户主体对象，具体类型是：UsernamePasswordAuthenticationToken
     * @return boolean ture-有权限，false-无权限
     * @author wangweijun
     * @since 2023/12/5 11:11
     */
    boolean check(HttpServletRequest request, Authentication authentication);
}
