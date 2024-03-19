package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端信息表：oauth2-registered-client-schema.sql
 * 授权确认表：oauth2-authorization-consent-schema.sql
 * 授权信息表：oauth2-authorization-schema.sql
 */
@SpringBootApplication
public class AuthServerJdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerJdbcApplication.class, args);
    }

    @RestController
    @RequestMapping("/receiver")
    public class Receiver {

        @GetMapping("/code")
        public String code(String code) {
            System.out.println("code = " + code);
            return code;
        }

    }
}