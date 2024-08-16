package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
        // 授权策略配置
        http.authorizeHttpRequests(authorize ->
                // 对所有请求开启授权保护
                authorize.anyRequest().authenticated()
        );

        // 使用默认表单授权方式
        http.formLogin(form -> {
            // 自定义登录页面
            form.loginPage("/login")
                    // 设置登录需要的用户名参数
                    .usernameParameter("username")
                    // 设置登录需要的密码参数
                    .passwordParameter("password")
                    // 认证成功后的处理
                    .successHandler(new JsonAuthenticationSuccessHandler())
                    // 认证失败后的处理，如果同时也定义了 failureUrl("/login?error")，那么最后定义的生效
                    .failureHandler(new JsonAuthenticationFailureHandler())
                    // 当前登录页面不需要认证
                    .permitAll();
        });

        // 注销配置
        http.logout(logout -> {
            logout.logoutSuccessHandler(new JsonLogoutSuccessHandler());
        });

        // 异常处理
        http.exceptionHandling(exception -> {
            // 请求未认证处理
            exception.authenticationEntryPoint(new JsonAuthenticationEntryPoint());
            // 权限访问拒绝处理
            exception.accessDeniedHandler(new JsonAccessDeniedHandler());
        });

        // 关闭 CSRF 保护
        http.csrf(AbstractHttpConfigurer::disable);
        // 开启 CORS 访问
        http.cors(Customizer.withDefaults());
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
