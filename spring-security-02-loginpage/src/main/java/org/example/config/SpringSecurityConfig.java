package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringSecurityConfig
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 11:15:50
 *
 * 注解 @EnableWebSecurity：开启 SpringSecurity 之后，会注册大量的过滤器，过滤器链【责任链模式】
 */
@EnableWebSecurity // 表示开启 SpringSecurity 支持
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(registry ->
                // requestMatchers("/login") 登陆接口
                // permitAll：具有所有权限，也就是可以匿名访问
                registry.requestMatchers("/login", "/user/login").permitAll()
                        // anyRequest：其他所有请求
                        // authenticated：都需要认证
                        .anyRequest().authenticated()
        );

        httpSecurity.formLogin(fromLogin ->
                // loginPage("/login")：指定登陆页面，如果要指定静态页面的画，要将 html 放在 static 下，并指定为 fromLogin.loginPage("/login.html")
                // permitAll：具有所有权限，也就是可以匿名访问
                // fromLogin.loginPage("/login.html").permitAll()
                fromLogin.loginPage("/login").permitAll()
                        // loginProcessingUrl("/user/login")：和前端访问地址对应：<form th:action="@{/user/login}" method="post">
                        .loginProcessingUrl("/user/login")
                        // defaultSuccessUrl("/index")：登陆之后跳转的接口 /index
                        .defaultSuccessUrl("/index")
        );

        // 跨域漏洞防御：默认关闭
        // 等同：httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.csrf(Customizer.withDefaults());

        // 跨域拦截：默认关闭
        httpSecurity.cors(Customizer.withDefaults());

        // 退出
        // logout.invalidateHttpSession(true)：session 失效
        httpSecurity.logout(logout -> logout.invalidateHttpSession(true));


        return httpSecurity.build();
    }

}
