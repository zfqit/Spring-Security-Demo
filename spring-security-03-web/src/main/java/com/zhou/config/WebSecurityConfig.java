package com.zhou.config;

import com.zhou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author zhoufuqi
 * @date 2023/2/13
 */
@Configuration
public class WebSecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/login.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin() // 开启表单校验
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin") // 配置登录地址,让其不被拦截
                .usernameParameter("uname")
                .passwordParameter("passwd")
                .defaultSuccessUrl("/index.html", true) // 设置登录后页面 redirect
                .failureUrl("/login.html")
                .and()
                .logout()
                .logoutUrl("/logout") // 设置注销 url
                .logoutSuccessUrl("/login.html") // 设置注销后退出页面 redirect
                .and()
                .csrf().disable();
        return http.build();
    }

    /**
     * 配置 AuthenticationManager 管理认证
     *
     * @param objectPostProcessor
     * @return org.springframework.security.authentication.AuthenticationManager
     * @author zhoufuqi
     * @date 2023/2/13 16:57
     */
    @Bean
    public AuthenticationManager auth(ObjectPostProcessor<Object> objectPostProcessor) throws Exception {
        AuthenticationManagerBuilder auth = new AuthenticationManagerBuilder(objectPostProcessor);
        auth.userDetailsService(userService);
        return auth.build();
    }

}
