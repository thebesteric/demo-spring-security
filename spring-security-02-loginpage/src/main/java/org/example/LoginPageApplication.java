package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * LoginPageApplication
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 11:14:41
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LoginPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoginPageApplication.class, args);
    }
}