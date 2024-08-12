package io.github.thebesteric.framework.agile.controller;

import io.github.thebesteric.framework.agile.domain.UserVO;
import io.github.thebesteric.framework.agile.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-09 12:02:54
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public List<UserVO> getUserList() {
        return userService.listUsers();
    }

}
