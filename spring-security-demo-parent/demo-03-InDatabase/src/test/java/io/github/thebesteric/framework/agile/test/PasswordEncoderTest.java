package io.github.thebesteric.framework.agile.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoderTest
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-09 15:22:22
 */
class PasswordEncoderTest {

    @Test
    void test() {
        String rawPassword = "123456";
        String encodedPassword = "$2a$10$J0pejOT3x9sm0.xJTipSOOf1ZDYQZNPboVEmeIM4PxfTGDpurfAPS";

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("encode(password) = " + passwordEncoder.encode(rawPassword));

        Assertions.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "密码不一致");
    }

}
