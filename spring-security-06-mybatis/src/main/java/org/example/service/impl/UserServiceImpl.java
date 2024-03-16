package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.PermissionVO;
import org.example.domain.UserVO;
import org.example.entity.User;
import org.example.mapper.RoleMapper;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

        return user;
    }
}
