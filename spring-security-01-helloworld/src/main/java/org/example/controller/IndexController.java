package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-11-24 10:40:53
 */
@RestController
@RequestMapping("/index")
public class IndexController {


    @GetMapping
    public String index() {
        return "Hello Spring Security";
    }

}
