package com.zhou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhoufuqi
 * @date 2023/2/10
 */
@Controller
public class UserController {

    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }
}
