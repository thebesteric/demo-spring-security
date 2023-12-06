package com.example.security.service;

import com.example.entity.Permission;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

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
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 查找用户所有角色
        List<String> roles = user.getRoles().stream().map(role -> {
            String tag = role.getTag();
            if (!role.getTag().startsWith("ROLE_")) {
                tag = "ROLE_" + tag;
            }
            return tag;
        }).toList();

        // 查找用户所有权限
        List<String> permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(Permission::getTag).toList();

        // 合并角色和权限数据
        List<String> roleAndPermissions = Stream.concat(roles.stream(), permissions.stream()).toList();

        // 利用 AuthorityUtils 将权限转换为 GrantedAuthority
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(roleAndPermissions);

        // 返回 UserDetails 接口类型对象，可以使用 new User() 或者 User.build() 的方式创建
        // return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorityList);
        return org.springframework.security.core.userdetails.User.withUsername(username).password(user.getPassword()).authorities(authorityList).build();
    }
}
