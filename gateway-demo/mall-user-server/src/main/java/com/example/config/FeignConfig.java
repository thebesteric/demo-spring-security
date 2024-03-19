package com.example.config;

import com.example.interceptor.FeignAuthRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FeignConfig
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-19 11:13:59
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignAuthRequestInterceptor();
    }

}
