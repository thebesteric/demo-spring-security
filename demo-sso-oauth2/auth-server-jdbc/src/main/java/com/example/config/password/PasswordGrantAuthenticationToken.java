package com.example.config.password;

import com.example.config.password.constant.OAuth2Constant;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * PasswordGrantAuthenticationToken
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-20 09:43:33
 */
public class PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    public PasswordGrantAuthenticationToken(Authentication clientPrincipal, @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2Constant.GRANT_TYPE_PASSWORD), clientPrincipal, additionalParameters);
    }
}
