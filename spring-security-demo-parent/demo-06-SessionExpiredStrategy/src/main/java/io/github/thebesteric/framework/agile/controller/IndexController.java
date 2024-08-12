package io.github.thebesteric.framework.agile.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IndexController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-08 18:14:29
 */
@Controller
@RequestMapping
public class IndexController {

    @RequestMapping(value = "/")
    public String index() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        // 认证信息
        Object principal = authentication.getPrincipal();
        System.out.println("principal = " + principal);

        return "index";
    }

}
