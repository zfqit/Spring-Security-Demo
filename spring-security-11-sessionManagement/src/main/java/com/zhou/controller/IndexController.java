package com.zhou.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoufuqi
 * @date 2023/04/11
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        System.out.println("index ok");
        return "index ok";
    }

}
