package com.zhou.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: TestController
 * Package: com.zhou.controller
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-17:06
 * @Version: v1.0
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "test";
    }

}
