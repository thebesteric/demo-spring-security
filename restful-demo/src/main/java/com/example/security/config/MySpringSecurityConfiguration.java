package com.example.security.config;

import com.example.security.filter.VerifyTokenFilter;
import com.example.security.handler.MyAccessDeniedHandler;
import com.example.security.handler.MyAuthenticationEntryPoint;
import com.example.security.handler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 * MySpringSecurityConfiguration
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-01 13:17:34
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class MySpringSecurityConfiguration {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private VerifyTokenFilter verifyTokenFilter;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    /**
     * 创建 PasswordEncoder 类型的 bean 对象
     */
    @Bean
    PasswordEncoder myPasswordEncoder() {
        // return new MyPasswordEncoder();
        // 强散列加密器，构造方法可以传递整型参数，范围 4～31，数字越大，强度越高，性能越低，默认是：10
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 配置登录相关的信息
        // 在 Security 中，登录页面地址和登录请求地址，默认都是不校验权限和认证的，但是建议把登录页面地址和登录请求地址都设置在授权配置中
        // 在 Security 中，登录成功后默认重定向到最近一次访问地址，如果最近访问地址式根或登录页面地址，则重定向到根，可以修改
        // 在 Security 中，认证失败后，默认重定向到登录页面到 url?error，此默认信息在 AbstractAuthenticationFilterConfigurer 定义了

        // 将拦截器加入在 UsernamePasswordAuthenticationFilter 之前
        httpSecurity.addFilterBefore(verifyTokenFilter, UsernamePasswordAuthenticationFilter.class);


        // 配置授权相关的信息
        // 在 Security 中，登录页面地址和登录请求地址，默认都是不校验权限和认证的，但是建议把登录页面地址和登录请求地址都设置在授权配置中
        // 通配符：
        // ?：代表一个字符
        // *：匹配零个或多个字符
        // **：匹配零个或多个目录
        // 常用方法：
        // requestMatchers(String,,, patterns)：指定路径
        // requestMatchers(HttpMethod method, String,,, patterns)：指定请求方法 + 路径
        httpSecurity.authorizeHttpRequests((configurer)-> {
            configurer
                    // 登陆请求可以直接访问，不需要任何权限
                    .requestMatchers( "/rest/login/page","/rest/login").anonymous()
                    // static 目录下 js、css 文件夹下的所有资源都不需要认证
                    .requestMatchers("/js/**").permitAll()
                    // anyRequest 代表一切请求，相当于 requestMatchers("/**")，所有该方法必须在最后调用
                    .anyRequest().authenticated(); // 其他所有请求，必须先登录认证，才可以访问
        });

        // 异常处理
        Customizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigurerCustomizer = configurer -> {
            // 权限异常：设置自定义的 403 访问无权限处理器
            configurer.accessDeniedHandler(myAccessDeniedHandler);
            // 登录异常：登录失败的处理逻辑
            configurer.authenticationEntryPoint(myAuthenticationEntryPoint);
        };
        httpSecurity.exceptionHandling(exceptionHandlingConfigurerCustomizer);

        // 登出处理
        Customizer<LogoutConfigurer<HttpSecurity>> logoutConfigurerCustomizer = configurer -> {
            configurer.logoutSuccessHandler(myLogoutSuccessHandler);
        };
        httpSecurity.logout(logoutConfigurerCustomizer);

        // 关闭 csrf 功能
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }
}
