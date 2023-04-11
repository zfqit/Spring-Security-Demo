package com.zhou.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author zhoufuqi
 * @date 2023/04/11
 */
@Configuration
public class SecurityConfig {

    private final FindByIndexNameSessionRepository findByIndexNameSessionRepository;

    @Autowired
    public SecurityConfig(FindByIndexNameSessionRepository findByIndexNameSessionRepository) {
        this.findByIndexNameSessionRepository = findByIndexNameSessionRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .and()
                .csrf().disable()
                // 开启回话管理
                .sessionManagement()
                // 限制用户登录的账号的最大并发
                .maximumSessions(1)
                // 用户被挤下线后跳转路径, 前后端不分离
                .expiredUrl("/login")
                // 设置前后端分离的响应结果
                .expiredSessionStrategy(event -> {
                    HttpServletResponse response = event.getResponse();
                    response.setContentType("application/json;charset=UTF-8");
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("code", 400);
                    result.put("msg", "用户被挤下线了");
                    response.getWriter().println(new ObjectMapper().writeValueAsString(result));
                })
                // 设置 redis 源
                .sessionRegistry(springSessionBackedSessionRegistry())
                .and().and().build();
    }

    /**
     * 设置 session 的监听, 用来监听用户回话
     * 适用于单机
     *
     * @return
     */
    //@Bean
    //public HttpSessionEventPublisher httpSessionEventPublisher() {
    //    return new HttpSessionEventPublisher();
    //}

    /**
     * 设置 redis 源用来存储用户回话
     *
     * @return
     */
    @Bean
    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry() {
        return new SpringSessionBackedSessionRegistry(findByIndexNameSessionRepository);
    }


}
