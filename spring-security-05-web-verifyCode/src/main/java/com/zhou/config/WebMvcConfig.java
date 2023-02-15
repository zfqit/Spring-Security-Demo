package com.zhou.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: WebMvcConfig
 * Package: com.zhou.config
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-11:51
 * @Version: v1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("/login");
        registry.addViewController("/index.html").setViewName("/index");
    }
}
