package com.example.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * LoginViewController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-12-02 23:36:43
 */
@Controller
public class LoginViewController {

    @RequestMapping("/login")
    public String loginView() {
        return "login";
    }

    // 如果使用请求转发：successForwardUrl("/main") 来跳转到这里到话
    // 那么由于登录处理时 POST 请求，所以这里必须设置为支持 POST
    @RequestMapping("/main")
    public String mainView() {
        return "main";
    }

    @RequestMapping("/loginFail")
    public String loginFailView() {
        return "fail";
    }


    @PostAuthorize("hasAuthority('reg:register-page')")
    @RequestMapping("/reg/register-page")
    public String registerPage() {
        return "register-page";
    }
}
