package com.zhou.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoufuqi
 * @date 2023/2/10
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello spring security";
    }
}
