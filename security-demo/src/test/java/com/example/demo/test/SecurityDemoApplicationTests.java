package com.example.demo.test;

import com.example.demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityDemoApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void test() {
        System.out.println(userMapper.selectUserByUsername("admin"));
    }

}
