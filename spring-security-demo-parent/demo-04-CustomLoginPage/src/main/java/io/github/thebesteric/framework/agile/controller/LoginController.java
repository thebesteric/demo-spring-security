package io.github.thebesteric.framework.agile.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * LoginController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-09 16:01:31
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        System.out.println("============== login ==============: " + request.getRequestURI());
        return "login";
    }

}
