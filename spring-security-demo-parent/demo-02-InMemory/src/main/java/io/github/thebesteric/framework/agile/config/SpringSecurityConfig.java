package io.github.thebesteric.framework.agile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
    /**
     * 利用 InMemoryUserDetailsManager 创建内存对象
     *
     * @return UserDetailsService
     *
     * @author wangweijun
     * @since 2024/8/9 11:26
     */
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        // 构建 UserDetails 对象
        UserDetails user = User.builder()
                .username("admin") // 自定义用户名
                .password("{noop}123456") // 自定义密码
                .roles("USER") // 自定义角色
                .build();
        manager.createUser(user);
        return manager;
    }
}
