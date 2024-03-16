# Spring Security 配置（前后端分离）
使用`successHandler`和`failureHandler`来控制登陆
```java
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 凭证
        Object credentials = authentication.getCredentials();
        // 身份
        Object principal = authentication.getPrincipal();
        // 权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(String.format("Login OK: [登陆凭证：%s, 身份：%s, 权限: %s]", credentials, principal, authorities));
    }
}
```
```java
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("Login Failed: " + exception.getMessage());
        exception.printStackTrace();
    }
}
```
注意跨域问题
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 是否发送 Cookie
                .allowCredentials(true)
                // 放行哪些原始域
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}
```