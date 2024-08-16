package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringSecurityConfig
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-08 20:37:21
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 配置授权策略
                .authorizeHttpRequests(authorize ->
                        authorize
                            // 对所有请求开启授权保护
                            .anyRequest().authenticated()
                )
                // 使用默认表单授权方式
                .formLogin(form -> {
                    // 自定义登录页面
                    form.loginPage("/login")
                            // 表单提交后跳转的路径，和表单提交的接口是一致的，可以不填写
                            .loginProcessingUrl("/login")
                            // 自定义用户名字段，默认: username
                            .usernameParameter("username")
                            // 自定义密码字段，默认: password
                            .passwordParameter("password")
                            // 认证成功后跳转的路径
                            .defaultSuccessUrl("/index.html")
                            // 登录失败后跳转地址
                            .failureUrl("/login?error")
                            // 放行 /login 接口请求
                            .permitAll();

                });

        // 关闭 CSRF 保护
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public InDBUserDetailsManager userDetailsManager(UserMapper userMapper) {
        return new InDBUserDetailsManager(userMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
