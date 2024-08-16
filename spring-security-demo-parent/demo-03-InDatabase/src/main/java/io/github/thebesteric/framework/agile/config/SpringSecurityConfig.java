package io.github.thebesteric.framework.agile.config;

import io.github.thebesteric.framework.agile.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                        // 对所有请求开启授权保护
                        authorize.anyRequest().authenticated()
                )
                // 使用默认表单授权方式
                .formLogin(Customizer.withDefaults());
                // 使用基本授权方式
                // .httpBasic(Customizer.withDefaults());

        // 关闭 CSRF 保护
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserMapper userMapper) {
        return new InDBUserDetailsManager(userMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
