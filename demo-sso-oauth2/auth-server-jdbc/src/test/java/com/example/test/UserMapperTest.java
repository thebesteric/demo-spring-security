package com.example.test;

import com.example.domain.UserVO;
import com.example.mapper.PermissionMapper;
import com.example.mapper.RoleMapper;
import com.example.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * UserMapperTest
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-29 13:03:31
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void testPermissionMapper() {
        System.out.println(permissionMapper.selectPermissionByRoleId(1L));
    }

    @Test
    void testRoleMapper() {
        System.out.println(roleMapper.selectRoleByUserId(1L));
    }

    @Test
    void test() {
        UserVO userVO = userMapper.selectUserByUsername("admin");
        System.out.println(userVO);
    }
}
