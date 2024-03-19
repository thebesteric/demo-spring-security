package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * GatewayOauth2Application
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-19 10:20:05
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GatewayOauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(GatewayOauth2Application.class, args);
    }
}
