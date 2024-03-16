package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * HelloWorldApplication
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 10:40:15
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class HelloWorldApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }
}
