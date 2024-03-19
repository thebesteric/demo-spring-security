package com.example.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthenticationController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-18 16:32:22
 */
@RestController
public class AuthenticationController {

    /**
     * 通过 OAuth2AuthorizedClient 对象，获取到客户端和令牌到相关信息，然后返回给前端
     * 这个接口生产上不需要
     */
    @GetMapping("/token")
    public OAuth2AuthorizedClient token(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        // 通过 OAuth2AuthorizedClient 对象，获取到客户端和令牌到相关信息，然后返回给前端
        return client;
    }

}
