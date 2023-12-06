package com.example;

import com.example.entity.Permission;
import com.example.entity.User;
import com.example.mapper.PermissionMapper;
import com.example.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class DemoSpringSecurityApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Test
    void selectUserByUsername() {
        User admin = userMapper.selectUserByUsername("admin");
        System.out.println(admin);
    }

    @Test
    void selectPermissionByUserId() {
        List<Permission> permissions = permissionMapper.selectPermissionByUserId(1L);
        System.out.println(permissions);
    }

    @Test
    void testBCryptPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        for (int i = 0; i < 3; i++) {
            String encode = passwordEncoder.encode("123456");
            System.out.println(encode);
            System.out.println(passwordEncoder.matches("123456", encode));
        }
    }

}
