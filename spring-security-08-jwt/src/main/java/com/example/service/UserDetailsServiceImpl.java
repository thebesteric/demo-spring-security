package com.example.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Fox
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里可以从数据库获取用户信息，参考 spring-security-06-mybatis
        return new User("admin", "{noop}123456",
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin,ROLE_user"));
    }
}
