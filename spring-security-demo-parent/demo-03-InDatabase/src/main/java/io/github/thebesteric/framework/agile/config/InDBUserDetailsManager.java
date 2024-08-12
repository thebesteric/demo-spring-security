package io.github.thebesteric.framework.agile.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.thebesteric.framework.agile.domain.PermissionVO;
import io.github.thebesteric.framework.agile.domain.UserVO;
import io.github.thebesteric.framework.agile.entity.User;
import io.github.thebesteric.framework.agile.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * DBUserDetailServiceManager
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-09 13:56:50
 */
@RequiredArgsConstructor
public class InDBUserDetailsManager implements UserDetailsService {

    private final UserMapper userMapper;

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

        // 这里用户的密码和 org.example.config.SpringSecurityConfig#passwordEncoder 有关系

        return user;
    }
}
