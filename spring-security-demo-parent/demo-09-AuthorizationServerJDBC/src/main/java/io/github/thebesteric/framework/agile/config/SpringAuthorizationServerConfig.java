package io.github.thebesteric.framework.agile.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.thebesteric.framework.agile.mapper.UserMapper;
import io.github.thebesteric.framework.agile.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * SpringAuthorizationServerConfig
 *
 * 访问地址：http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=oidc-client&scope=profile%20openid&redirect_uri=http://www.baidu.com
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-15 11:51:38
 */
@Configuration
@EnableWebSecurity
public class SpringAuthorizationServerConfig {

    /**
     * Spring Security 的过滤器链，用于协议端点的
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        // 将默认的 OAuth2 security configuration 应用到 HttpSecurity
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 开启 OpenID Connect 1.0
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());

        http
                // 将需要认证的请求，重定向到 /login 页面进行登录
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // 使用 JWT 处理接收到的 access_token
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * Spring Security 的过滤器链，用于 Spring Security 的身份验证
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置请求处理方式
                .authorizeHttpRequests(authorize ->
                        authorize
                                // 放行 OPTIONS, 解决 Token 请求跨域问题
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                // 对所有请求都拦截
                                .anyRequest().authenticated())
                // 由 Spring Security 过滤链中的 UsernamePasswordAuthenticationFilter 过滤器拦截处理
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    // /**
    //  * 基于内存的用户信息
    //  */
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails admin = User.builder()
    //             .username("admin")
    //             .password("{noop}123456")
    //             .roles("ADMIN")
    //             .build();
    //
    //     UserDetails user = User.builder()
    //             .username("user")
    //             .password("{noop}123456")
    //             .roles("USER")
    //             .build();
    //     return new InMemoryUserDetailsManager(admin, user);
    // }

    // /**
    //  * 基于内存的客户端信息
    //  */
    // @Bean
    // public RegisteredClientRepository registeredClientRepository() {
    //     RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
    //             .clientId("oidc-client")
    //             .clientSecret("{noop}secret")
    //             // 客户端授权模式：这里使用的是 BASIC 认证
    //             .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
    //             // 授权模式（支持配置多个）：授权码模式
    //             .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
    //             // 授权模式（支持配置多个）：刷新令牌
    //             .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
    //             // 授权模式（支持配置多个）：客户端模式
    //             .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
    //             // 回调客户端的 URI（支持配置多个），会携带 code 参数
    //             .redirectUri("http://127.0.0.1:8001/login/oauth2/code/oidc-client")
    //             .redirectUri("http://www.baidu.com")
    //             // .postLogoutRedirectUri("http://127.0.0.1:9000/")
    //             // 设置权限范围
    //             .scope(OidcScopes.OPENID)
    //             .scope(OidcScopes.PROFILE)
    //             // 是否需要手动点击确认授权
    //             .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
    //             .build();
    //
    //     return new InMemoryRegisteredClientRepository(oidcClient);
    // }

    /**
     * 基于数据库的用户信息
     */
    @Bean
    public UserDetailsService userDetailsService(UserMapper userMapper) {
        return new UserServiceImpl(userMapper);
    }

    /**
     * 客户端信息
     * 对应表：oauth2-registered-client-schema.sql
     * spring-security-oauth2-authorization-server-1.3.1.jar!/org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * 授权信息
     * 对应表：oauth2_authorization
     * spring-security-oauth2-authorization-server/1.3.1/spring-security-oauth2-authorization-server-1.3.1.jar!/org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 授权确认
     * 对应表：oauth2_authorization_consent
     * spring-security-oauth2-authorization-server/1.3.1/spring-security-oauth2-authorization-server-1.3.1.jar!/org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 密码解析器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置 JWK，为 JWT(id_token) 提供加密密钥，用于加密/解密或签名/验签
     * JWK 详细见：https://datatracker.ietf.org/doc/html/draft-ietf-jose-json-web-key-41
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 生成 RSA 密钥对，给上面 jwkSource() 方法的提供密钥对
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        // 什么都不配置，则使用默认地址，即请求的 token 的地址就是 iss 的地址
        return AuthorizationServerSettings.builder().build();
    }
}
