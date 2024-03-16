# Spring Security 配置
```java
package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(registry ->
                // requestMatchers("/login") 登陆页面
                // permitAll：具有所有权限，也就是可以匿名访问
                registry.requestMatchers("/login").permitAll()
                        // anyRequest：其他所有请求
                        // authenticated：都需要认证
                        .anyRequest().authenticated()
        );

        httpSecurity.formLogin(fromLogin ->
                // loginPage("/login")：登陆页面
                // permitAll：具有所有权限，也就是可以匿名访问
                fromLogin.loginPage("/login").permitAll()
                        // loginProcessingUrl("/login")：转到登陆接口 /login
                        .loginProcessingUrl("/login")
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

```
# 认证源码分析
1. 在那里做的登陆？
2. 用户存储在那里？我们是不是可以自己添加多个用户？
3. 退出接口在那里？

用户名和密码存储，默认是内存实现，也就是用户名和密码提前写死 \
`InMemoryUserDetailsManager`实现了`UserDetailsManager`
```java
public class InMemoryUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(SecurityProperties properties, ObjectProvider<PasswordEncoder> passwordEncoder) {
        SecurityProperties.User user = properties.getUser();
        List<String> roles = user.getRoles();
        return new InMemoryUserDetailsManager(new UserDetails[]{User.withUsername(user.getName()).password(this.getOrDeducePassword(user, (PasswordEncoder) passwordEncoder.getIfAvailable())).roles(StringUtils.toStringArray(roles)).build()});
    }
}
```
`UserDetailsManager` 继承了`UserDetailsService`
```java
public interface UserDetailsManager extends UserDetailsService {
    void createUser(UserDetails user);
    void updateUser(UserDetails user);
    void deleteUser(String username);
    void changePassword(String oldPassword, String newPassword);
    boolean userExists(String username);
}
```
如果需要**改造和数据库整合**，可以从这里下手，来**设置数据源**，关键是：**实现`UserDetailsService`接口**，来替换默认的`InMemoryUserDetailsManager`
```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```
当我们启动项目当时候，会执行`SpringSecurityConfig`的`securityFilterChain`方法中的`formLogin`，在`formLogin`中，创建了`new FormLoginConfigurer()`
```java
public FormLoginConfigurer() {
    super(new UsernamePasswordAuthenticationFilter(), (String)null);
    this.usernameParameter("username");
    this.passwordParameter("password");
}
```
如果我们想自定义一个登陆 filter，就需要继承`AbstractAuthenticationProcessingFilter`