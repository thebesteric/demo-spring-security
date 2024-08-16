package io.github.thebesteric.framework.agile.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthenticationController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-15 17:06:37
 */
@RestController
public class AuthenticationController {

    @GetMapping("/token")
    public OAuth2AuthorizedClient token(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient;
    }

}
