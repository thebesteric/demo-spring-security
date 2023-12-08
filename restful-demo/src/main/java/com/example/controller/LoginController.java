package com.example.controller;

/**
 * LoginController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-06 23:52:58
 */

import com.example.convert.UserConvert;
import com.example.domain.MyUserDetails;
import com.example.domain.R;
import com.example.domain.UserDomain;
import com.example.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/rest/login")
public class LoginController {

    @Autowired
    public AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserConvert userConvert;

    @GetMapping("/page")
    public String loginPage() {
        return "login";
    }

    @PostMapping
    @ResponseBody
    public R<Map<String, Object>> login(@RequestBody Map<String, Object> params) throws Exception {
        String username = params.get("username").toString();
        String password = params.get("password").toString();

    // @PostMapping
    // @ResponseBody
    // public R<Map<String, Object>> login(String username, String password) throws Exception {

        // 将收到的 username 和 password 封装为 UsernamePasswordAuthenticationToken 对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 将 token 对象交给 AuthenticationManager 处理
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        // 这里会调用 UserDetailsService 的 loadUserByUsername 方法，同时调用 PasswordEncoder 比对密码
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 将验证成功的对象保存到上下文
        SecurityUtils.setAuthentication(authentication);

        // 从认证对象中获取 MyUserDetailsForJson 对象
        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();

        // 生成 token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 将 token 做为 key，principal 做为 value 存入 redis
        UserDomain userDomain = principal.getUserDomain();
        userDomain.setPassword(null);
        redisTemplate.opsForValue().set(token, userDomain, Duration.ofSeconds(3600));

        return R.success(Map.of("user", userDomain, "token", token));
    }

}
