package com.example.security.config;

import com.example.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

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

    /**
     * 创建 PasswordEncoder 类型的 bean 对象
     */
    @Bean
    PasswordEncoder myPasswordEncoder() {
        // return new MyPasswordEncoder();
        // 强散列加密器，构造方法可以传递整型参数，范围 4～31，数字越大，强度越高，性能越低，默认是：10
        return new BCryptPasswordEncoder(8);
    }

    /**
     * 记住我
     * 使用 Security 框架提供的实现类即可：JdbcTokenRepositoryImpl
     * JdbcTokenRepositoryImpl：基于数据源，访问指定的数据库，把认证成功的用户数据保存到数据库
     */
    @Bean
    PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 初始化参数，即第一次启动项目时，设置为 true，后期再启动修改为 false
        // 如果设置为 true，启动时会自动创建表，表名就是 persistent_logins，创建完成后，一定要设置为 false，否则会抛出：表已存在的异常
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 配置登录相关的信息
        // 在 Security 中，登录页面地址和登录请求地址，默认都是不校验权限和认证的，但是建议把登录页面地址和登录请求地址都设置在授权配置中
        // 在 Security 中，登录成功后默认重定向到最近一次访问地址，如果最近访问地址式根或登录页面地址，则重定向到根，可以修改
        // 在 Security 中，认证失败后，默认重定向到登录页面到 url?error，此默认信息在 AbstractAuthenticationFilterConfigurer 定义了

        // 使用匿名内部类，创建自定义配置信息
        Customizer<FormLoginConfigurer<HttpSecurity>> loginConfigurerCustomizer = config -> {
            // 进行具体的配置处理
            // 设置登录页面，此时可以通过控制器+视图的方式，自定义登录页面，必须是 GET 请求，默认也是 GET /login
            config.loginPage("/login")
                    // 设置登录逻辑请求地址，此请求必须是 POST，默认是 POST /login，这里不需要创建 controller
                    // 这里的 url 会赋值给 AbstractAuthenticationFilterConfigurer 的 loginProcessingUrl
                    .loginProcessingUrl("/userLogin")
                    // 自定义登录的用户名和密码名称
                    .usernameParameter("uname").passwordParameter("pwd")

                    // 设置默认的登录成功后的跳转地址，注意：仅在访问登录页面时，才会生效
                    // .defaultSuccessUrl("/main")
                    // 设置登录认证成功后，转发到到地址，全局生效
                    // .successForwardUrl("/main")
                    // 设置认证后的代码处理逻辑，参数是：AuthenticationSuccessHandler，由于方法会向上覆盖，所以可以取消：defaultSuccessUrl 和 successForwardUrl
                    .successHandler(new LoginSuccessHandler("/main", true))

                    // 设置默认的登录失败后的跳转地址，重定向处理
                    // .failureUrl("/loginFail")
                    // 认证失败后，转发
                    // .failureForwardUrl("/loginFail")
                    // 设置认证后的代码处理逻辑，参数：AuthenticationFailureHandler，由于方法会向上覆盖，所以可以取消：failureUrl 和 failureForwardUrl
                    .failureHandler(new LoginFailHandler("/loginFail", true));

        };
        httpSecurity.formLogin(loginConfigurerCustomizer);

        // 配置授权相关的信息
        // 在 Security 中，登录页面地址和登录请求地址，默认都是不校验权限和认证的，但是建议把登录页面地址和登录请求地址都设置在授权配置中
        // 通配符：
        // ?：代表一个字符
        // *：匹配零个或多个字符
        // **：匹配零个或多个目录
        // 常用方法：
        // requestMatchers(String,,, patterns)：指定路径
        // requestMatchers(HttpMethod method, String,,, patterns)：指定请求方法 + 路径
        httpSecurity.authorizeRequests()

                // 自定义权限访问
                // .requestMatchers("/**").access("@myPermissionCheckImpl.check(request, authentication)");

                // 登陆请求可以直接访问，不需要任何权限
                .requestMatchers("/login", "/userLogin", "/loginFail").permitAll()

                // static 目录下 js、css 文件夹下的所有资源都不需要认证
                .requestMatchers("/js/**", "/css/**", "/images/**").access("permitAll")

                // 测试 403 用，权限时不存在的
                .requestMatchers("/user").hasAuthority("user:manage")

                // anyRequest 代表一切请求，相当于 requestMatchers("/**")，所有该方法必须在最后调用
                .anyRequest().authenticated(); // 其他所有请求，必须先登录认证，才可以访问

        // 配置 remember-me 相关信息
        Customizer<RememberMeConfigurer<HttpSecurity>> rememberMeConfigurerCustomizer = configurer -> {
            configurer.tokenRepository(persistentTokenRepository())
                    // 设置 remember-me 的请求参数名称，默认：remember-me
                    .rememberMeParameter("remember-me")
                    // 设置客户端 cookie 的名称，默认：remember-me
                    .rememberMeCookieName("REMEMBER_ME")
                    // 设置客户端 cookie 的 domain
                    .rememberMeCookieDomain("127.0.0.1")
                    // 设置客户端 cookie 的过期时间，默认 1800s
                    .tokenValiditySeconds(60 * 60 * 5)
                    // 设置自定义 userDetailsService 接口的实现对象，在用户第一次登录时，调用 service 查询用户
                    .userDetailsService(userDetailsService);
        };
        httpSecurity.rememberMe(rememberMeConfigurerCustomizer);

        // 退出登录配置（建议，值增加额外处理逻辑，可选退出登录由的请求地址，其他不建议修改）
        Customizer<LogoutConfigurer<HttpSecurity>> logoutConfigurerCustomizer = configurer -> {
            configurer
                    // 退出登录的请求地址，不需要自己实现 controller
                    .logoutUrl("/logout")
                    // 退出登录后，请求的地址
                    .logoutSuccessUrl("/login")
                    // 设置退出登录后的处理代码逻辑。需要 LogoutSuccessHandler 的子类
                    .logoutSuccessHandler(new MyLogoutSuccessHandler())
                    // 增加额外的退出成功后的处理代码逻辑，会在 logoutSuccessHandler 之前执行，需要 LogoutHandler 的子类
                    .addLogoutHandler(new MyLogoutHandler());
        };
        httpSecurity.logout(logoutConfigurerCustomizer);

        // 异常处理
        Customizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigurerCustomizer = configurer -> {
            // 设置自定义的 403 访问无权限处理器
            configurer.accessDeniedHandler(new MyAccessDeniedHandler());
            // 登录失败的处理逻辑，通常很少自定义
            configurer.authenticationEntryPoint(new MyAuthenticationEntryPoint());
        };
        httpSecurity.exceptionHandling(exceptionHandlingConfigurerCustomizer);

        // csrf 功能，默认开启
        // httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }
}
