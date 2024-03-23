package com.example.demo.service.impl;

import com.example.demo.domain.MyUserDetails;
import com.example.demo.domain.UserDomain;
import com.example.demo.service.PermissionService;
import com.example.demo.utils.SecurityUtils;
import org.springframework.stereotype.Service;

/**
 * 自定义权限校验器
 * 可以使用 @PreAuthorize("@myAuth.hasAuthority('user:test')") 方式鉴权
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-08 16:09:34
 */
@Service("myAuth")
public class PermissionServiceImpl implements PermissionService {
    @Override
    public boolean hasAuthority(String authority) {
        MyUserDetails userDetails = (MyUserDetails) SecurityUtils.getLoginUser();
        if (userDetails == null) {
            return false;
        }
        // 管理员直接放行
        UserDomain userDomain = userDetails.getUserDomain();
        if (userDomain.isAdmin()) {
            return true;
        }

        // 匹配权限
        return userDomain.getAuthorities().stream().anyMatch(auth -> authority.equals(auth.getAuthority()));
    }
}
