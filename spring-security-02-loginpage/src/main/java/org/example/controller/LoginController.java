package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * LoginController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 11:01:42
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String login() {
        return "login";
    }
}
