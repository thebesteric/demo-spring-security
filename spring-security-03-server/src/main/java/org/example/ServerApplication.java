package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 前后端分离模式
 * 通过：successHandler 和 failureHandler 来实现，通过 exceptionHandling 可以控制访问受限后的处理
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 11:14:41
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}