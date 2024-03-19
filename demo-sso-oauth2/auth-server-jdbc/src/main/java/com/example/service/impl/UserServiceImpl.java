package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.PermissionVO;
import com.example.domain.UserVO;
import com.example.entity.User;
import com.example.mapper.RoleMapper;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServiceImpl
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-25 17:21:20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户未找到");
        }

        // 查找权限
        UserVO userVO = userMapper.selectUserByUsername(username);
        List<String> permissions = userVO.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream()).map(PermissionVO::getTag).toList();
        user.setAuthorities(AuthorityUtils.createAuthorityList(permissions));

        // 这里用户的密码和 org.example.config.SecurityConfig#passwordEncoder 有关系

        List<SimpleGrantedAuthority> grantedAuthorityList = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorityList);

        // TODO 这里直接返回 user 会有如下问题，待解决：The class with com.example.entity.User and name of com.example.entity.User is not in the allowlist
        // return user;
    }
}
