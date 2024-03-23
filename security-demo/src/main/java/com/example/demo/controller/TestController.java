package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-03-23 17:04:12
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public String get() {
        return "get";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('user:add')")
    public String add() {
        return "userAdd";
    }

}
