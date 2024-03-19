package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserService
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-25 17:20:25
 */
public interface UserService extends IService<User>, UserDetailsService {
}
