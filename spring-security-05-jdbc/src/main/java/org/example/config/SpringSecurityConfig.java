package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * SpringSecurityConfig
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 11:15:50
 * <p>
 * 注解 @EnableWebSecurity：开启 SpringSecurity 之后，会注册大量的过滤器，过滤器链【责任链模式】
 */
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(registry ->
                registry
                        // ***************** 角色 *****************
                        // .requestMatchers("/access/admin/api").hasRole("admin")
                        // .requestMatchers("/access/user/api").hasAnyRole("user", "admin")

                        // ***************** 权限 *****************
                        .requestMatchers("/access/admin/api").hasAuthority("admin:api")
                        .requestMatchers("/access/user/api").hasAnyAuthority("admin:api", "user:api")
                        .requestMatchers("/access/admin/api/save").hasAuthority("admin:api")
                        .requestMatchers("/access/user/api/query").hasAnyAuthority("admin:api", "user:api")
                        .requestMatchers("/access/app/api").permitAll()

                        // ***************** ant 匹配 *****************
                        // ?: 任意单个字符
                        // *: 0 到任意数量个字符
                        // **: 到任意目录
                        .requestMatchers("/access/test/api/**").hasAuthority("admin:api")
                        .anyRequest().authenticated()


        );

        // 异常信息，现在通过异常信息配置一个未授权页面
        // 实际上是不合理的，应该通过异常信息来判断是什么异常
        httpSecurity.exceptionHandling(e -> e.accessDeniedPage("/access/refused"));

        httpSecurity.formLogin(fromLogin ->
                        fromLogin
                                // 指定登陆页
                                .loginPage("/login").permitAll()
                                // loginProcessingUrl("/login")：转到登陆接口 /login
                                .loginProcessingUrl("/login")
                                // successHandler：登陆成功处理器
                                // .successHandler(new LoginSuccessHandler())
                                .defaultSuccessUrl("/index")
                // failureHandler：登陆失败处理器
                // .failureHandler(new LoginFailureHandler())
        );

        // 跨域漏洞防御：默认关闭
        httpSecurity.csrf(Customizer.withDefaults());

        // 跨域拦截：默认关闭
        httpSecurity.cors(Customizer.withDefaults());


        return httpSecurity.build();
    }


    @Bean
    public UserDetailsService inMemoryUserDetailsManager() {

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);

        UserDetails admin = User.withUsername("admin").password("123456").authorities("admin:api", "user:api").build();
        UserDetails user = User.withUsername("user").password("123456").authorities("user:api").build();

        // 在表中创建用户信息
        if (!userDetailsManager.userExists("admin")) {
            userDetailsManager.createUser(admin);
        }

        if (!userDetailsManager.userExists("user")) {
            userDetailsManager.createUser(user);
        }


        return userDetailsManager;
    }

    /**
     * PasswordEncoder：加密编码器
     * 实际开发中，开发环境一般是明文加密，生产环境是密文加密，也就是可以配置多种加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 明文加密
        return NoOpPasswordEncoder.getInstance();
    }

}
