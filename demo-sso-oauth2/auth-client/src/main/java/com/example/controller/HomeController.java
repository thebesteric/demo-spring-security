package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HomeController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-18 16:31:08
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String index() {
        return "index";
    }

}
