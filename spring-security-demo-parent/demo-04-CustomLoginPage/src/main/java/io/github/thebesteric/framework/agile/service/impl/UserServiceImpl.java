package io.github.thebesteric.framework.agile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thebesteric.framework.agile.domain.UserVO;
import io.github.thebesteric.framework.agile.entity.User;
import io.github.thebesteric.framework.agile.mapper.UserMapper;
import io.github.thebesteric.framework.agile.service.UserService;
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

    @Override
    public List<UserVO> listUsers() {
        return getBaseMapper().listUsers();
    }
}
