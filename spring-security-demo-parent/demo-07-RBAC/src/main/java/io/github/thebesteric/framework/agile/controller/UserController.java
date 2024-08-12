package io.github.thebesteric.framework.agile.controller;

import io.github.thebesteric.framework.agile.domain.UserVO;
import io.github.thebesteric.framework.agile.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/list")
    public List<UserVO> getUserList() {
        return userService.listUsers();
    }

    @PreAuthorize("hasAuthority('user:add') and authentication.name == 'admin'")
    @GetMapping("/add")
    public String add() {
        return "add user successfully";
    }

}
