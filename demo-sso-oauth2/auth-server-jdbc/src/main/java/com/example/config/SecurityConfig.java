package com.example.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
 * SecurityConfig
 *
 * 参考：https://docs.spring.io/spring-authorization-server/reference/getting-started.html
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-18 09:55:28
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    /**
     * Spring Authentication Server 相关配置
     * 主要配置 OAuth 2.1 和 OpenID Connect 1.0
     *
     * 授权服务器信息：http://127.0.0.1:9000/.well-known/openid-configuration
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authenticationServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 开启 OpenID Connect 1.0，oidc 就是 OpenID Connect 的缩写
                .oidc(Customizer.withDefaults());

        httpSecurity
                // 当未从授权端点进行身份验证时，重定向到登录页面
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // 使用 jwt 处理接收到到 token
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 对所有请求都拦截
                .authorizeHttpRequests((authorize)-> authorize
                        .requestMatchers("/receiver/code").permitAll()
                        .anyRequest().authenticated())
                // 由 Spring Security 过滤链中的 UsernamePasswordAuthenticationFilter 过滤器拦截处理
                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails userDetails = User.withDefaultPasswordEncoder()
    //             .username("user")
    //             .password("123456")
    //             .roles("USER")
    //             .build();
    //     // 基于内存的用户数据校验
    //     return new InMemoryUserDetailsManager(userDetails);
    // }

    // @Bean
    // public RegisteredClientRepository registeredClientRepository() {
    //     RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
    //             .clientId("oidc-client")
    //             // {noop} 开头，表示以明文信息存储密码 “secret”
    //             .clientSecret("{noop}secret")
    //             // 授权方式：Basic Auth
    //             .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
    //             // 配置授权码模式
    //             .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
    //             // 配置刷新令牌
    //             .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
    //             // 配置客户端模式
    //             .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
    //             .redirectUri("http://oauth-client:9001/login/oauth2/code/oidc-client")
    //             // .redirectUri("http://127.0.0.1:9001/login/oauth2/code/messaging-client-oidc")
    //             // 配置到 baidu 这个域名来接收授权码 code
    //             // .redirectUri("http://www.baidu.com")
    //             .redirectUri("http://127.0.0.1:9000/receiver/code")
    //             .postLogoutRedirectUri("http://127.0.0.1:8080/")
    //             // 设置客户端权限范围
    //             .scope(OidcScopes.OPENID)
    //             .scope(OidcScopes.PROFILE)
    //             // 客户端设置用户需要确认授权，如果设置为 false，则表示不需要客户端确认
    //             .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
    //             .build();
    //
    //     return new InMemoryRegisteredClientRepository(oidcClient);
    // }

    /**
     * 客户端信息
     * 对应表：oauth2_registered_client
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * 授权信息
     * 对应表：oauth2_authorization
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 授权确认
     *对应表：oauth2_authorization_consent
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
     * JWK详细见：https://datatracker.ietf.org/doc/html/draft-ietf-jose-json-web-key-41
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

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 配置授权服务器请求地址
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        // 什么都不配置，则使用默认地址
        return AuthorizationServerSettings.builder().build();
    }

}