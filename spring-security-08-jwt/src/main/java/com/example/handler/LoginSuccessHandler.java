package com.example.handler;

import com.alibaba.fastjson.JSON;
import com.example.utils.JWTUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    // 秘钥
    private String secret = "123456xxxx";

    // 30分钟过期，可根据实际情况自行修改;
    private long expMillis = 3600000;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        User user = (User) principal;
        // 1.从authentication 取出用户信息，保存到claims map对象
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("authorities", user.getAuthorities());
        claims.put("enabled", user.isEnabled());
        claims.put("expiresIn", (System.currentTimeMillis() + expMillis));
        // 2.生成token
        String token = JWTUtils.getAccessToken(secret, claims);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", token);
        // 3.将token以JSON串返回前端
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }
}
