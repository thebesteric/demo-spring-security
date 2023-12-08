package com.example.security.service.impl;

import com.example.convert.UserConvert;
import com.example.domain.MyUserDetails;
import com.example.domain.UserDomain;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 登陆服务实现类型
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-01 13:24:55
 */
@Component
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConvert userConvert;

    /**
     * 根据用户名查询用户和用户的权限列表
     *
     * @param username 用户名
     * @return UserDetails
     * @author wangweijun
     * @since 2023/12/1 13:26
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("【自定义登陆服务】loadUserByUsername 方法执行");

        // 根据用户名查询用户
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 封装为 UserDomain 对象
        UserDomain userDomain = userConvert.toUserDomain(user);

        // 返回 MyUserDetails
        return new MyUserDetails(userDomain);
    }
}
