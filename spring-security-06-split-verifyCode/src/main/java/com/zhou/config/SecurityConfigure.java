package com.zhou.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.security.filter.LoginVerifyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: SecurityConfigure
 * Package: com.zhou.config
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-15:23
 * @Version: v1.0
 */
@Configuration
public class SecurityConfigure {


    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.builder().username("root").password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    @Bean
    public AuthenticationManager auth(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService());
        return auth.build();
    }

    @Bean
    public LoginVerifyFilter loginVerifyFilter(HttpSecurity http) throws Exception {
        LoginVerifyFilter filter = new LoginVerifyFilter();
        filter.setVerifyCode("verifyCode");
        filter.setUsernameParameter("uname");
        filter.setPasswordParameter("password");
        filter.setAuthenticationManager(auth(http));
        filter.setFilterProcessesUrl("/doLogin");
        filter.setAuthenticationFailureHandler((request, response, exception) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("msg", exception.getMessage());
            result.put("data", exception);
            String s = new ObjectMapper().writeValueAsString(result);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(s);
        });
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", 200);
            map.put("msg", "登录成功");
            map.put("data", authentication);
            String s = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(s);
        });
        return filter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().mvcMatchers("/verify").permitAll().anyRequest().authenticated().and().formLogin().and().exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            Map<String, Object> map = new HashMap<>();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("code", 500);
            map.put("msg", authException.getMessage());
            String s = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(s);
        }).and().logout().and().csrf().disable();
        http.addFilterAt(loginVerifyFilter(http), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
