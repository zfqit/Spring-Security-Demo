package com.zhou.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoufuqi
 * @date 2023/2/14
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "test ok";
    }
}
