package io.github.thebesteric.framework.agile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringAuthorizationServerConfig
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-15 11:51:38
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // 所有的访问都需要通过身份认证
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults())
                        // 认证异常处理
                        .authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                        // 没有权限的异常处理
                        .accessDeniedHandler(new JsonAccessDeniedHandler())

                );
        return http.build();
    }
}
