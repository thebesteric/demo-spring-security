package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain authorizationClientSecurityFilterChain(HttpSecurity http) throws Exception {

                http
                        // 请求路径管理
                        .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/assets/**", "/webjars/**", "/index").permitAll()
                                .anyRequest().authenticated()
                        )
                        // Session会话管理
                        .sessionManagement(sessionManagementConfigurer->{
                            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
                        })
                        //OAuth2.0登录配置
                        .oauth2Login(oauth2LoginCustomizer->{
                            // 设置用以获取token请求的客户端
                            oauth2LoginCustomizer.tokenEndpoint(tokenEndpointCustomizer->{
                                //采用DefaultAuthorizationCodeTokenResponseClient默认处理
                                tokenEndpointCustomizer.accessTokenResponseClient(new DefaultAuthorizationCodeTokenResponseClient());
                            });
                        })
                ;

        return http.build();
    }

}
