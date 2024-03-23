package com.example.demo.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * SecurityUtils
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-06 23:09:09
 */
public class SecurityUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void clearContext() {
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(emptyContext);
    }

    public static UserDetails getLoginUser() {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails;
        }
        return null;
    }

    public static String encryptPassword(String rawPassword) {
        return encryptPassword(rawPassword, 10);
    }

    public static String encryptPassword(String rawPassword, int strength) {
        return new BCryptPasswordEncoder(strength).encode(rawPassword);
    }

}
