package io.github.thebesteric.framework.agile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thebesteric.framework.agile.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserService
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-25 17:20:25
 */
public interface UserService extends IService<User>, UserDetailsService {
}
