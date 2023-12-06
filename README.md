# 密码解析器
Security 框架定义的接口是`PasswordEncoder`\
Security 框架强制要求，必须在 Spring 容器中存在`PasswordEncoder`类型的对象且唯一

# 定义登陆服务类型
Security 框架定义了一个登录服务接口，类型是：`UserDetailsService`\
此接口中定义了一个唯一的登录服务方法，方法是：`UserDetails loadUserByUsername(String username) throw UsernameNotFoundException`\
此方法用于查询登陆用户，仅需要根据用户名查询用户对象并返回，如果用户名查询无结果，抛出：UserNotFoundException\
方法的返回值是`UserDetails`类型的接口，接口中定义了若干方法，重点是：**获取用户对象**和**获取用户权限列表**，此接口必须自定义实现类型并被 Spring 容器管理。
```java
public interface UserDetails extends Serializable {
    // 返回登陆用户的权限列表
    Collection<? extends GrantedAuthority> getAuthorities();
    // 返回登陆用户密码（密文）
    String getPassword();
    // 返回登陆用户的用户名
    String getUsername();
    // 返回登陆用户是否未过期，返回 true，未过期
    boolean isAccountNonExpired();
    // 返回登陆用户是否未锁定，返回 true，未锁定
    boolean isAccountNonLocked();
    // 返回登陆用户凭证是否未过期，返回 true，未过期，如：身份证
    boolean isCredentialsNonExpired();
    // 返回登陆用户是否可用，返回 true，可用
    boolean isEnabled();
}
```
Security 提供了`UserDetails`类型的接口的实现，实现类是：`org.springframework.security.core.userdetails.User`\
同时也提供了一个工具类`AuthorityUtils`来将我们定义的权限转换未 Security 定义的`GrantedAuthority`类型的权限对象

# 简述 Security 认证流程
1. 请求当前项目任意地址，除 /login 外，检查是否已经登录，未登录自动会重定向到登陆页，已登录则正常访问
   - `GET /login`：进入登陆页面
   - `POSt /login`：请求登陆
2. 填写登录表单（用户名、密码），点击登录，访问 `POST /login`
3. Security 框架负责收集请求参数，`username` 和 `password`
4. Security 框架从 Spring 容器中，根据类型获取 `UserDetailService` 的 bean 对象\
   - `ApllicationContext.getBean(UserDetailService.class)`
5. Security 框架调用 `loadUserByUsername` 方法，根据用户名查询用户对象
   - 如果 `loadUserByUsername` 方法抛出了异常 `UsernameNotFoundException`，直接重定向到 `/login?error`，登陆失败
   - 如果 `loadUserByUsername` 方法未抛出异常，则校验返回值中保存的密码和请求参数的密码是否匹配
6. 获取 Spring 容器中的 `PsswordEncoder` 类型的 bean 对象，调用 `matches` 方法比较密码
   - 如果密码比较结果是 true，登陆成功，重定向到 `/上次请求地址?continue`，登陆成功
   - 如果密码比较结果是 false，登陆失败，重定向到 `/login?error`，登陆失败

# 认证安全强化
在数据库中保存加密后的密码数据，修改密码解析器 `PsswordEncoder` 的实现对象\
Security 框架提供了若干密码解析器的实现类型，其中 `BCryptPasswordEncoder` 叫强散列加密
- 强散列加密器，构造方法可以传递整型参数 `new BCryptPasswordEncoder(int strength)`，范围 4～31，数字越大，强度越高，性能越低，默认是：10
- 可以保障相同的明文，多次加密后，密文有相同的散列数据，不同的结果
- 匹配时，是基于相同的散列数据做的匹配

# 自定义配置
> Spring Boot 3.x + JakartaEE + Spring Security 6.x 版本，修改了自定义配置策略\
> 从 Spring Boot 2. + JavaEE + Spring Security 5.x 版本的 WebSecurityConfigure 自动装配策略改为：Configuration + Bean 对象配置策略\
> 要求：定义 Configuration 配置类型，定义 Bean 对象管理方法，来创建 `SecurityFilterChain` 类型对象 \
> Security 框架，自动在 Spring 容器中创建一个 `HttpSecurity` 类型的对象，可以通过方法参数传递，来创建 `SecurityFilterChain` 类型的对象\
> `SecurityFilterChain` 不需要 new，可以通过 `HttpSecurity` 的 build 来创建
使用 Configuration 实现配置，覆盖 Security 默认的配置信息，继承 Security 框架提供的 `SecurityConfigurerAdapter` 适配器类型。
- 其中 `SecurityFilterChain` 用于提供 Servlet 技术中的过滤器链
- 其中 `HttpSecurity` 用于提供 Security 框架中的配置处理，包含**登录认证配置**，**授权配置**，**csrf 配置**

Security 框架官方推荐，在提供 Security 配置对象的类型上加上 `@EnableWebSecurity` 注解，让 Security 框架一定生效

# 自定义登录流程
- 自定义登录页面
action 是表单请求的地址，Security 框架提供了一个默认的请求地址：POST /login\
请求参数名，默认式 username 和 password，请求参数可以通过过滤器实现，可以通过配置修改请求参数名称，参考：`UsernamePasswordAuthenticationFilter`
```html
<form action="/userLogin" method="post">
   <table>
       <tr>
           <td>用户名</td>
           <td><input type="text" name="uname" value="admin" /></td>
       </tr>
       <tr>
           <td>密码</td>
           <td><input type="password" name="pwd" value="123456" /></td>
       </tr>
       <tr>
           <td colspan="2"><input type="submit" value="登录" /></td>
       </tr>
   </table>
</form>
```
- 定义登录请求地址
- 定义登录请求参数名称
- 定义登录成功后的处理
- 定义登录失败后的处理

1. 请求发送到 `UsernamePasswordAuthenticationFilter` 过滤器
2. 执行 `attemptAuthentication` 方法，此方法由其父类 `AbstractAuthenticationProcessingFilter` 的 `doFilter()` 来调用的
3. `attemptAuthentication` 执行时，会先获取请求参数，默认是：`username` 和 `password`，具体由`obtainUsername()` 和 `obtainPassword()` 处理的
4. 创建认证方法参数对象 `UsernamePasswordAuthenticationToken` 此对象为登录请求参数
5. 再调用认证 `authenticate()` 方法，方法参数为：`UsernamePasswordAuthenticationToken` 对象，并将 `authenticated` 字段标记为 `false` 表示未认证
6.  `authenticate()` 方法是由接口 `AuthenticationManager` 的实现类 `ProviderManager` 提供实现的
7. `ProviderManager` 中核心方法是 `Authentication result = provider.authenticate(authentication);`
   - 核心方法由接口：`AuthenticationProvider` 的实现类 `AbstractUserDetailsAuthenticationProvider` 提供具体实现
   - 首先通过 `determineUsername()` 判断 `authentication.getPrincipal() == null` 请求参数是否存在身份（用户名）
   - 检查是否存在缓存：`UserDetails user = this.userCache.getUserFromCache(username);`
   - 如果缓存中没有要登录的用户对象：在通过 `retrieveUser()` 方法，调用 `loadUserDetails()` 方法
     - `retrieveUser()` 方法，是一个抽象方法，具体由 `DaoAuthenticationProvider` 来实现
     - `DaoAuthenticationProvider` 的 `retrieveUser()` 方法会调用自定义的 `UserDetailsService` 实现类的 `loadUserByUsername()` 方法，获取到 `UserDetails` 类型的用户
8. 最后通过 `UserDetailsChecker.check()` 方法，检查 `UserDetails` 用户对象，分为前置校验、密码校验、后置三次检查
   - 前置校验：`DefaultPreAuthenticationChecks.check()`，检查用户是否被锁定、是否不可用、账号是否过期
   - 密码校验：抽象方法 `additionalAuthenticationChecks()`，检查密码是否正确，具体是由 `DaoAuthenticationProvider` 的 `additionalAuthenticationChecks()` 实现的
     - `DaoAuthenticationProvider` 的 `additionalAuthenticationChecks()` 会调用：`passwordEncoder.matches()` 方法，比对密码
   - 前置校验：`DefaultPostAuthenticationChecks.check()`，检查用户密码是否过期
9. 如果没有任何异常，则创建对象 `this.createSuccessAuthentication(principalToReturn, authentication, user);` 并返回 `Authentication` 类型对象
   - 返回的对象类型是 `Authentication` 类型，具体实现类是 `UsernamePasswordAuthenticationToken`
   - 至此 `AbstractUserDetailsAuthenticationProvider.authenticate()` 方法运行完毕，返回：`UsernamePasswordAuthenticationToken` 对象
   - 此时会将 `UsernamePasswordAuthenticationToken` 对象中的 `authenticated` 字段标记为 `true` 表示已认证
10. `ProviderManager` 对查询结果的密码做隐藏处理，将 `UsernamePasswordAuthenticationToken` 强转为 `CredentialsContainer` 类型，调用 `eraseCredentials` 方法
    - `((CredentialsContainer)result).eraseCredentials();`，隐藏密码的目的是数据脱敏
    - 返回已登录的 `UsernamePasswordAuthenticationToken` 对象，是 `Authentication` 的实现类，此时密码已经脱敏
11. 父类型的 `AbstractAuthenticationProcessingFilter` 的 `doFilter()` 方法继续执行
    - 没有抛出异常，执行 `successfulAuthentication()` 进入登录认证成功处理流程，调用 `AuthenticationSuccessHandler` 的 `onAuthenticationSuccess()`
    - 抛出异常，执行 `unsuccessfulAuthentication()` 进入登录认证失败处理流程，调用 `AuthenticationFailureHandler` 的 `onAuthenticationFailure()`

# Security 附加功能 - remember me
可以让同一个客户端，减少登录次数
1. 登录参数请求，前端页面必须新增一个 `remember-me`，值是 `true`，代表：记住我，参数名称可以修改
2. 使用配置的方式，提供必要信息
```java
@Bean
PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
    jdbcTokenRepository.setDataSource(dataSource);
    // 初始化参数，即第一次启动项目时，设置为 true，后期再启动修改为 false
    // 如果设置为 true，启动时会自动创建表，表名就是 rememberMe
    jdbcTokenRepository.setCreateTableOnStartup(true);
    return jdbcTokenRepository;
}

// 代码片段
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
    // 设置自定义 userDetailsService 接口的实现对象
    .userDetailsService(userDetailsService);
};
httpSecurity.rememberMe(rememberMeConfigurerCustomizer);
```

# 退出登录
Security 框架中，默认提供了退出登录的功能
> 建议：只增加额外处理逻辑，可选退出登录由的请求地址，其他不建议修改
- 此功能请求的默认地址是 `GET /logout`，可以使用配置的方式进行修改
- 直接请求 `GET /logout` 自动实现退出登录逻辑
- 退出登录时，会清楚应用内存中的用户信息，销毁会话对象，删除 remember-me 信息
- 退出登录后，访问的地址默认时，登录页面的地址?logout，默认是：`/login?logout`
  - 退出登录后的跳转地址，可以在 `LogoutConfigurer` 类型中定义的
```java
// 代码片段
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
```
# 授权管理
Security 框架中的授权管理，需要先提供用户的权限列表
1. 先修改 `UserDetailsService` 中的代码逻辑，增加认证用户的权限数据
- 注意：Security 框架对权限的管理是基于：权限地址 + 权限字符串描述 来实现的
- 请求地址和权限字符串描述，都是字符串类型
- Security 框架也可以处理角色权限，基于角色名称管理权限，角色名称同样是字符串，为了区分角色名称和权限，角色名称必须以 `ROLE_` 开头
2. 通过配置类型，做初步权限控制

## Security 基础权限
- anonymous：必须未登录才可以访问
- authenticated：必须登录才可以访问
- fullyAuthenticated：必须完成登录，才可以访问，不可以使用 rememberMe 方式登录，用于敏感操作，如：支付，改密码
- rememberMe：必须使用 rememberMe 方式访问
- permitAll：无权限校验，登录、未登录都可以访问
- denyAll：不可以访问

## Security 框架中的相对细致的权限管理
包括：针对角色的权限管理、针对权限描述的权限管理、针对客户端访问 IP 的权限管理
- 角色管理
  - `hasRole`：登录用户权限列表中，是否存在某个角色，参数是角色名称，不含前缀 `Role_`
  - `hasAnyRole`：登录用户权限列表中，有任意一个角色，都可以访问，参数是角色名称，不含前缀 `Role_`
- 权限管理
  - `hasAuthority`：登录用户权限列表中，是否存在某个权限，参数是权限描述
  - `hanAnyAuthority`：登录用户权限列表中，有任意一个权限，都可以访问，参数是权限描述
- 客户端 IP 管理
- `hasIPAddress`：访问客户端 IP 地址限制

## 权限底层设计
- anonymous：access("anonymous")
- authenticated：access("authenticated")
- fullyAuthenticated：access("fullyAuthenticated")
- rememberMe：access("rememberMe")
- permitAll：access("permitAll")
- denyAll：access("denyAll")
- hasRole("角色名")：access("hasRole(ROLE_ + 角色名)")
- hasAnyRole("角色名1", "角色名2")：access("hasRole(ROLE_ + 角色名, ROLE_ + 角色名)")
- hasAuthority("权限描述")：access("hasAuthority(权限描述)")
- hanAnyAuthority("权限描述1", "权限描述2")：access("hanAnyAuthority(权限描述1, 权限描述2)")
- hasIPAddress("IP")：access("hasIPAddress(IP)")

## 自定义权限管理
> 不推荐使用，强制要求前端请求接口的时候携带特定参数
1. 定义一个接口，接口中定义唯一的方法，方法要求：`boolean 方法名(HttpServletRequest req, Authentication authentication)`
2. 实现类必须被 Spring 所管理
3. 编写配置信息，可以使用 SpringEL 表达式
   - @bean 对象名称，可以从容器中获取 bean 对象
   - @bean 对象名称.方法名称，可以调用对象的方法
   - 可以通过某对象的 property 属性名，直接为方法传递参数，要求对象必须被 Spring 容器管理，且对象信息为配置或管理，
     - 比如：Security 框架中的 WebSecurityExpressionRoot，类型名存在 Expression 的大部分都是 SpringEL 可以直接访问属性的

Security 框架的 access 方法的返回值，如果是 true 代表有权限，false 代表无权限
```java
@Component
public class MyPermissionCheckImpl implements MyPermissionCheck {

    public static final List<String> ignorePaths = List.of("/login", "/userLogin", "/loginFail", "/js", "/css");

    /**
     * 每次请求都传递一个参数：参数名是：perm，值是：user:add
     * 权限检查时，设定规则如下
     * 1. 如果路径是 /login，/userLogin，/loginFail 则不检查权限，直接放行
     * 2. 其他的访问路径地址。则获取请求参数 perm，然后判断参数值是否是用户拥有的权限
     *
     * @param request        HttpServletRequest
     * @param authentication 认证用户
     * @return boolean
     * @author wangweijun
     * @since 2023/12/5 11:14
     */
    @Override
    public boolean check(HttpServletRequest request, Authentication authentication) {
        // 1. 获取本次请求的路径
        String path = request.getServletPath();
        // 是否需要放行
        if (ignorePaths.stream().anyMatch(path::startsWith)) {
            return true;
        }

        // 2. 获取请求的权限参数
        String perm = request.getParameter("perm");
        perm = (perm != null && !perm.trim().isEmpty()) ? perm.trim() : "none";
        
        // 判断是否登录
        if (authentication != null && authentication.isAuthenticated()) {
            // 获取当前用户的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // 权限是否存在当前请求需要的权限
            if (authorities.contains(new SimpleGrantedAuthority(perm))) {
                return true;
            }
        }

        return false;
    }
}

// 配置类的相关配置
httpSecurity.authorizeRequests().requestMatchers("/**").access("@myPermissionCheckImpl.check(request, authentication)");
```

# 自定义 403 显示问题
1. 编写 `AccessDeniedHandler` 接口实现类
2. 编写配置
```java
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 处理 403 的方式
     *
     * @param request               HttpServletRequest
     * @param response              HttpServletResponse
     * @param accessDeniedException 异常对象
     * @author wangweijun
     * @since 2023/12/5 18:16
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = accessDeniedException.getMessage();
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<h3 style='color: blue'>权限不足，请联系系统管理员：" + message + "</h3>");
        response.getWriter().println("<a href='/main'>返回首页</a>");
        response.getWriter().flush();
    }
}

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = authException.getMessage();
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<h3 style='color: blue'>登录失败：" + message + "</h3>");
        response.getWriter().println("<a href='/login'>返回登录</a>");
        response.getWriter().flush();
    }
}

// 异常处理
Customizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigurerCustomizer = configurer -> {
    // 设置自定义的访问无权限处理器
    configurer.accessDeniedHandler(new MyAccessDeniedHandler());
    // 登录失败的处理逻辑，通常很少自定义
    configurer.authenticationEntryPoint(new MyAuthenticationEntryPoint());
};
httpSecurity.exceptionHandling(exceptionHandlingConfigurerCustomizer);
```

# 注解 + 少量配置实现权限管理
- 配置：实现固定的权限管理，如，请求页面、登录页面、登录请求、登录错误、退出登录、异常处理、资源放行等
- 注解：提供各种具体服务的权限管理，如：查询，新增、修改、删除、权限管理、菜单管理等
使用注解进行权限管理，必须
1. 在启动类或者配置类上，增加 `@EnableMethodSecurity`
- 给注解增加属性，开启你需要的权限管理注解 `@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)`
- securedEnabled: 开启注解 `@Secured` 功能，用于角色权限管理
- prePostEnabled：开启注解 `@PreAuthorize` 和 `@PostAuthorize` 功能，用于基于表达式字符串的权限管理
- 表达式字符串就是 access 方法的参数表达式，比如："hasRole('ROLE_xxx')"
2. 在具体的方法上，通过注解 `@Secured` 实现权限管理。一般注解写在控制器控制单元方法上。

# Thymeleaf 中访问 Security 中的用户信息
在 thymeleaf-extras-springsecurity6 中提供了一个新的 thymeleaf 属性命名空间，名称空间是：sec
命名空间包含的属性有
- sec:authentication=""，此属性是管理访问 security 框架内存中的已登录用户对象，此对象类型是 `Authentication` 接口类型，具体是：`UsernamePasswordAuthenticationToken` 类型
  - 属性中直接写 `UsernamePasswordAuthenticationToken` 类型的属性名，可以直接获取属性的值
- sec:authorize=""，此属性类型权限管理注解 @PreAuthorize，属性的值就是 access 方法可以识别的所有权限表达式
  - 当权限表达式满足时，显示属性所在的标签，不满足，则此标签不存在
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
    <version>3.1.2.RELEASE</version>
</dependency>
```

# Java 代码访问 Security 中的用户信息
Security 认证流程结束后，会把登录成功的用户数据封装成 `Authentication` 类型对象，并保存在 HttpSession 会话中，有框架来保存，用户不知道 attribute 的名字\
Security 提供了一个工具类 `SecurityContextHolder`，并提供了一个静态方法 `getContext`，返回：`SecurityContext`，就是当前请求对应会话中保存的用户上下文对象
- `SecurityContext` 有 `Authentication getAuthentication()` 方法，返回 `Authentication` 对象

# CSRF
1. 当服务器加载登录页面时（loginPage 中的值，默认是 /login），先生成 CSRF 对象，并放入请求作用域中，key 为 `_csrf`。之后会对 `${_csrf.token}` 进行替换，替换成服务器生成的 token 字符串
2. 用户在提交表单数据时，会携带 csrf 的 token，如果客户端的 token 和服务器的 token 匹配，则验证通过。否则无法继续执行
3. 用户退出时，必须发起 POST 请求，且和登录一样，携带 csrf 的 token

- 开启 CSRF 之后，在登录和退出的时候必须发送 POST 请求（关闭 CSRF 的时候，退出可以是 GET 请求）
- 请求中必须额外携带一个参数。参数名是 `_csrf`，参数的值由服务器来提供，在 thymeleaf 中可以使用表达式获取令牌 `th:value = "${_csrf.token}"`
```html
<input type="hidden" name="_csrf" th:value="${_csrf.token}" />
```