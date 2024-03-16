package org.example.config;

import org.example.security.LoginFailureHandler;
import org.example.security.LoginSuccessHandler;
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
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(registry ->
                // requestMatchers("/login") 登陆接口
                // permitAll：具有所有权限，也就是可以匿名访问
                registry.requestMatchers("/login").permitAll()
                        // anyRequest：其他所有请求
                        // authenticated：都需要认证
                        .anyRequest().authenticated()
        );

        httpSecurity.formLogin(fromLogin ->
                fromLogin
                        // loginProcessingUrl("/login")：转到登陆接口 /login
                        .loginProcessingUrl("/login").permitAll()
                        // successHandler：登陆成功处理器
                        .successHandler(new LoginSuccessHandler())
                        // failureHandler：登陆失败处理器
                        .failureHandler(new LoginFailureHandler())
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
