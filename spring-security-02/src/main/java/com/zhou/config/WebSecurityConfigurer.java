package com.zhou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author zhoufuqi
 * @date 2023/2/10
 */
@Configuration
public class WebSecurityConfigurer {

    /**
     * 新版 spring security 配置
     * 对资源放行设置
     *
     * @param http
     * @return org.springframework.security.web.SecurityFilterChain
     * @author zhoufuqi
     * @date 2023/2/10 11:55
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().mvcMatchers("/index").permitAll().anyRequest().authenticated().and().formLogin();
        return http.build();
    }

}
