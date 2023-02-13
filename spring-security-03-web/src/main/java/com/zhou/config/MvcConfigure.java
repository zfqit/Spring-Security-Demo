package com.zhou.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhoufuqi
 * @date 2023/2/13
 */
@Configuration
public class MvcConfigure implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 设置资源路径访问
        registry.addViewController("/login.html").setViewName("/login");
        registry.addViewController("/index.html").setViewName("/index");
    }
}
