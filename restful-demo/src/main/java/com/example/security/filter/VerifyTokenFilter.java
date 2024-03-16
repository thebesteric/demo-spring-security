package com.example.security.filter;

import com.example.domain.MyUserDetails;
import com.example.domain.R;
import com.example.domain.UserDomain;
import com.example.util.SecurityUtils;
import com.example.util.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * VerifyTokenFilter
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-07 21:43:01
 */
@Component
public class VerifyTokenFilter extends HttpFilter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        if ("/rest/login/page".equals(requestURI) || "/rest/login".equals(requestURI) || requestURI.startsWith("/js")) {
            chain.doFilter(request, response);
            return;
        }

        // 1. 从请求中获取 token
        String token = request.getHeader("x-token");

        // 2. 判断 token 是否为空，并从 redis 查询 token 对应的用户信息

        if (StringUtils.isEmpty(token)) {
            ServletUtils.renderString(response, R.error(401, "[filter]: token 不存在，认证失败"));
            return;
        }

        Object value = redisTemplate.opsForValue().get(token);
        if (value == null) {
            ServletUtils.renderString(response, R.error(401, "[filter]: 用户不存在，认证失败"));
            return;
        }

        // 3. 设置到 SecurityContext 上下文中
        UserDomain userDomain = (UserDomain) value;
        MyUserDetails myUserDetails = new MyUserDetails(userDomain);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(myUserDetails, userDomain.getPassword(), userDomain.getAuthorities());
        SecurityUtils.setAuthentication(authentication);

        // 4. 放行
        chain.doFilter(request, response);
    }
}
