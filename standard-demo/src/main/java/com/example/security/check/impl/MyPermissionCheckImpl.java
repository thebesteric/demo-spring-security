package com.example.security.check.impl;

import com.example.security.check.MyPermissionCheck;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * MyPermissionCheckImpl
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-05 11:13:20
 */
@Component
public class MyPermissionCheckImpl implements MyPermissionCheck {

    public static final List<String> ignorePaths = List.of("/login", "/userLogin", "/loginFail", "/js", "/css");

    /**
     * 每次请求都传递一个参数：参数名是：perm，值是：user:add
     * 权限检查时，设定规则如下
     * 1. 如果路径是 /login，/userLogin，/loginFail 则不检查权限，直接放行
     * 2. 其他的访问路径地址。则获取请求参数 perm，然后判断参数值是否是用户拥有的权限
     *
     * @param request        HttpServletRequest
     * @param authentication 认证用户
     * @return boolean
     * @author wangweijun
     * @since 2023/12/5 11:14
     */
    @Override
    public boolean check(HttpServletRequest request, Authentication authentication) {
        // 1. 获取本次请求的路径
        String path = request.getServletPath();
        // 是否需要放行
        if (ignorePaths.stream().anyMatch(path::startsWith)) {
            return true;
        }

        // 2. 获取请求的权限参数
        String perm = request.getParameter("perm");
        perm = (perm != null && !perm.trim().isEmpty()) ? perm.trim() : "none";

        // 判断是否登录
        if (authentication != null && authentication.isAuthenticated()) {
            // 获取当前用户的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // 权限是否存在当前请求需要的权限
            if (authorities.contains(new SimpleGrantedAuthority(perm))) {
                return true;
            }
        }

        return false;
    }
}
