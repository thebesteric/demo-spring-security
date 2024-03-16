package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * LoginController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 11:01:42
 */
@Controller
@RequestMapping("/access")
public class AccessController {

    // 没有认证不可以访问
    @GetMapping("/refused")
    public String refused() {
        return "refused";
    }

    // admin 用户可以访问
    @GetMapping("/admin/api")
    public String admin() {
        return "admin-api";
    }

    @GetMapping("/admin/api/save")
    @ResponseBody
    public String adminSave() {
        return "admin-save";
    }

    // 普通用户可以访问
    @GetMapping("/user/api")
    public String user() {
        return "user-api";
    }

    @GetMapping("/user/api/query")
    @ResponseBody
    public String userQuery() {
        return "user-query";
    }

    // 所有用户可以访问
    @GetMapping("/app/api")
    public String app() {
        return "app-api";
    }

    @GetMapping("/test/api/a/b/c")
    @ResponseBody
    public String test() {
        return "test-api-a-b-c";
    }

}
