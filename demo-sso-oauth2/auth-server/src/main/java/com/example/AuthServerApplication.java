package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthServerApplication
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-18 09:54:02
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
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
