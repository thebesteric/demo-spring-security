package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IndexController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 10:40:53
 */
@Controller
@RequestMapping("/index")
public class IndexController {


    @GetMapping
    public String index() {
        return "index";
    }

}
