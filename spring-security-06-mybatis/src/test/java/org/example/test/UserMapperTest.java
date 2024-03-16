package org.example.test;

import org.example.domain.UserVO;
import org.example.mapper.UserMapper;
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

    @Test
    void test() {
        UserVO userVO = userMapper.selectUserByUsername("admin");
        System.out.println(userVO);
    }
}
