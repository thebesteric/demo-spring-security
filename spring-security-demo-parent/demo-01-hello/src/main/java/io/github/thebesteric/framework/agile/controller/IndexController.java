package io.github.thebesteric.framework.agile.controller;

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
        return "index";
    }

}
