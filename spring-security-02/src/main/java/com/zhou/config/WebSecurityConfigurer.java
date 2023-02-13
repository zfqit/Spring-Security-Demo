package com.zhou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

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
        http.authorizeHttpRequests().mvcMatchers("/index").permitAll().mvcMatchers("/login.html").permitAll() // 设置 /login.html /index 资源被放行
                .anyRequest().authenticated() // 设置所有请求拦截
                .and().formLogin() // 设置表单验证
                .loginPage("/login.html") // 设置自定义登录页
                .usernameParameter("uname")  // 设置表单 username 属性名
                .passwordParameter("passwd") // 设置表单 password 属性名
                .loginProcessingUrl("/doLogin") // 关闭 from 表单提交登录的请求的拦截,不关闭会导致又会重新被拦截到登录页面
                //.successForwardUrl("/hello") // 认证通过后,转发到指定的路径 具体转发到之前请求路径上
                //.defaultSuccessUrl("/hello") //  认证通过后,重定向(redirect)到指定的路径
                .successHandler(new MyAuthenticationSuccessHandler()) // 自定义登录成功的返回内容 用于前后端分离返回 json
                // 注意: SPRING_SECURITY_LAST_EXCEPTION 作为错误信息展示字段, failureForwardUrl 放在 request 中, failureUrl放在 session 中
                //.failureForwardUrl("/login.html") // failureForwardUrl 失败以后的 forward 跳转 注意:因此获取 request 中异常信息,这里只能使用failureForwardUrl`
                //.failureUrl("/login.html") // failureUrl 失败以后的重定向跳转
                .failureHandler(new MyAuthenticationFailureHandler()) // 自定义登录失败的返回内容 用于前后端分离返回 json
                .and().logout() // 开启注销配置
                //.logoutUrl("/logout") // 设置注销 url GET
                .logoutRequestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/aa", "GET"), new AntPathRequestMatcher("/bb", "POST"))) // 配置多组注销 url 和 请求方式
                .invalidateHttpSession(true) // 注销后清除 session
                .clearAuthentication(true) // 注销后清除认证
                //.logoutSuccessUrl("/login.html") // 退出登录时跳转地址
                .logoutSuccessHandler(new MyLogoutSuccessHandler()) // 自定义注销成功的返回内容 用于前后端分离返回 json
                .and().csrf().disable();//这里先关闭 CSRF;
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        UserDetails u1 = User.withUsername("zhangs").password("{noop}111").roles("USER").build();
        inMemoryUserDetailsManager.createUser(u1);
        return inMemoryUserDetailsManager;
    }

    /**
     * 自定义全局配置 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    //@Bean
    //public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    //    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    //    authenticationManagerBuilder.userDetailsService(userDetailsService());
    //    return authenticationManagerBuilder.build();
    //}


    /**
     * 自定义全局配置 AuthenticationManager
     *
     * @param objectPostProcessor
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authManager(ObjectPostProcessor<Object> objectPostProcessor) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
        authenticationManagerBuilder.userDetailsService(userDetailsService());
        return authenticationManagerBuilder.build();
    }

    /**
     * 旧版 spring security 配置
     * 对资源放行设置
     * 继承 WebSecurityConfigurerAdapter 重新 configure 方法
     */
    //@Override
    //protected void configure(HttpSecurity http) throws Exception {
    //    http.authorizeHttpRequests().mvcMatchers("/index").permitAll().anyRequest().authenticated().and().formLogin();
    //}

}
