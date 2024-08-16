package io.github.thebesteric.framework.agile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HomeController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2024-08-15 17:07:11
 */
@RestController
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "This is the home page";
    }
}
