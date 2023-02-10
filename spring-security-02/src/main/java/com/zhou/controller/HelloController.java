package com.zhou.controller;

import org.springframework.security.core.context.SecurityContextHolder;
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
        // 在 SecurityContextHolder 获取用户信息
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // 默认情况下 子线程是获取不到用户信息的
        // 需要配置策略才能获取用户信息的
        // 需要在VM Options 配置 -Dspring.security.strategy=MODE_INHERITABLETHREADLOCAL
        new Thread(() -> {
            System.out.println(SecurityContextHolder.getContext().getAuthentication());
        }).start();

        return "hello security";
    }


}
