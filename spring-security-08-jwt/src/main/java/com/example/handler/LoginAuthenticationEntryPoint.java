package com.example.handler;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Component
public class LoginAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 如果验证失败，统一返回JSON串，并将状态码设置为401，表示未授权
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter out = response.getWriter();
        Map<String,Object> data = new HashMap<>();
        data.put("path", request.getRequestURI());
        data.put("time", LocalDateTime.now().toString());
        data.put("errCode", HttpStatus.UNAUTHORIZED.value());
        data.put("errMsg", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        out.write(JSON.toJSONString(data));
        out.flush();
        out.close();
    }
}
