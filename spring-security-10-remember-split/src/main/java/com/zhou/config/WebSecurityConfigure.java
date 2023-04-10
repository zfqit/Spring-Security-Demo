package com.zhou.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.filter.MyUsernamePasswordAuthenticationFilter;
import com.zhou.security.MyPersistentTokenBasedRememberMeServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhoufuqi
 * @date 2023/2/14
 */
@Configuration
public class WebSecurityConfigure {


    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.builder().username("root").password("{noop}123").authorities(
                "admin").build());
        return inMemoryUserDetailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public MyUsernamePasswordAuthenticationFilter loginFilter(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        MyUsernamePasswordAuthenticationFilter loginFilter = new MyUsernamePasswordAuthenticationFilter();
        loginFilter.setPasswordParameter("password");
        loginFilter.setUsernameParameter("uname");
        loginFilter.setRememberMeServices(rememberMeServices());
        // 配置登录地址
        loginFilter.setFilterProcessesUrl("/doLogin");
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", 200);
            map.put("msg", "登录成功");
            map.put("data", authentication);
            String s = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(s);
        });
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", exception.getMessage());
            result.put("data", exception);
            String s = new ObjectMapper().writeValueAsString(result);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(s);
        });
        loginFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return loginFilter;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService());

        http.authorizeHttpRequests()
                .anyRequest().authenticated().and().formLogin()
                .and().exceptionHandling().authenticationEntryPoint((request, response, exception) -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("code", 500);
                    result.put("msg", exception.getMessage());
                    result.put("data", exception);
                    String s = new ObjectMapper().writeValueAsString(result);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(s);
                }).and().logout().logoutRequestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/logout", "GET"), new AntPathRequestMatcher("/logout", "POST"))) // 配置多组注销 url 和 请求方式
                .logoutSuccessHandler((request, response, authentication) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", 200);
                    map.put("msg", "注销成功");
                    map.put("data", authentication);
                    String s = new ObjectMapper().writeValueAsString(map);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(s);
                }).and()
                .rememberMe() // 开启记住我
                .rememberMeServices(rememberMeServices())
                .and()
                .csrf().disable();
        http.addFilterAt(loginFilter(authenticationConfiguration), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new MyPersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(), userDetailsService(), new InMemoryTokenRepositoryImpl());
    }

}
