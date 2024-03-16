package com.example.config;


import com.example.filter.JwtAuthenticationTokenFilter;
import com.example.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private LoginAuthenticationEntryPoint loginAuthenticationEntryPoint;
    @Autowired
    private BussinessAccessDeniedHandler bussinessAccessDeniedHandler;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 对请求进行访问控制设置
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                // 设置哪些路径可以直接访问，不需要认证
                .requestMatchers("/user/**").hasRole("admin")
                //.requestMatchers("/index").permitAll()
                .anyRequest().authenticated() // 其他路径的请求都需要认证
        );

        // 自定义登录逻辑
        http.formLogin((formLogin) -> formLogin
                .loginProcessingUrl("/login")// 登录访问路径：前台界面提交表单之后跳转到这个路径进行UserDetailsService的验证，必须和表单提交接口一样
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
        );

        // 添加 JWT 登录拦截器，在登录之前获取 token 并校验
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 访问受限后的异常处理
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(loginAuthenticationEntryPoint)
                .accessDeniedHandler(bussinessAccessDeniedHandler)
        );

        // 自定义退出登录逻辑
        http.logout((logout) -> logout.logoutSuccessHandler(myLogoutSuccessHandler));

        // 关闭跨站点请求伪造csrf防护
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


}
