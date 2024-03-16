package com.example.filter;


import com.example.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * 注意，这个 JwtAuthenticationTokenFilter 必须要在 UsernamePasswordAuthenticationFilter 之前
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1.从请求头中取出 token，进行判断，如果没有携带 token，则继续往下走其他的其他的 filter 逻辑
        String tokenValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(tokenValue)) {
            // 继续往后走，就会走到没有登录的异常
            filterChain.doFilter(request, response);
            return;
        }
        // 2. 校验 token
        // 2.1 将 token 切割前缀 “bearer ”，然后使用封装的 JWT 工具解析 token，得到一个 map 对象
        String token = tokenValue.substring("bearer ".length());
        Map<String, Object> map = JWTUtils.parseToken(token);
        // 2.2 取出 token 中的过期时间，调用 JWT 工具中封装的过期时间校验，如果 token 已经过期，则删除登录的用户，继续往下走其他 filter 逻辑
        if (JWTUtils.isExpiresIn((long) map.get("expiresIn"))) {
            // token 已经过期，清空授权对象
            SecurityContextHolder.getContext().setAuthentication(null);
            // 继续往后走，就会走到没有登录的异常
            filterChain.doFilter(request, response);
            return;
        }

        String username = (String) map.get("username");
        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 获取用户信息
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails != null && userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 设置用户登录状态
                log.info("Authenticated user {}, setting to security context", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}

