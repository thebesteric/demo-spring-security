package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
// 开启方法授权
@EnableMethodSecurity(jsr250Enabled = true, proxyTargetClass = true, securedEnabled = true)
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 授权策略配置
        http.authorizeHttpRequests(authorize ->
                // 对所有请求开启授权保护
                authorize
                        // 基于请求的授权：访问 /user/list 资源需要 user:list 权限
                        .requestMatchers("/user/list").hasAuthority("user:list")
                        // 基于请求的授权：访问 /user/add 资源需要 user:add 权限
                        .requestMatchers("/user/add").hasAuthority("user:add")
                        // 任何请求都需要认证
                        .anyRequest().authenticated()
        );

        // 使用默认表单授权方式
        http.formLogin(form -> {
            // 自定义登录页面
            form.loginPage("/login")
                    // 设置登录需要的用户名参数
                    .usernameParameter("username")
                    // 设置登录需要的密码参数
                    .passwordParameter("password")
                    // 认证失败后跳转的 URL 地址
                    .failureUrl("/login?error")
                    // 认证成功后的处理
                    .successHandler(new JsonAuthenticationSuccessHandler())
                    // 认证失败后的处理
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

        http.sessionManagement(session -> {
            // 设置最多有多 1 个 Session，也就是后登录的账号会让之前登录的账号过期
            session.maximumSessions(1)
                    // 设置 Session 过期后处理
                    .expiredSessionStrategy(new JsonSessionInformationExpiredStrategy());
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
