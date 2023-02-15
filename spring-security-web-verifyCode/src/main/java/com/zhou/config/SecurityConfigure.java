package com.zhou.config;

import com.zhou.security.filter.VerifyFilter;
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

/**
 * ClassName: SecurityConfigure
 * Package: com.zhou.config
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-11:30
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
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService());
        return builder.build();
    }

    @Bean
    public VerifyFilter verifyFilter(HttpSecurity http) throws Exception {
        VerifyFilter filter = new VerifyFilter();
        filter.setVerifyCode("verify");
        filter.setPasswordParameter("pwd");
        filter.setUsernameParameter("uname");
        filter.setFilterProcessesUrl("/doLogin");
        filter.setAuthenticationManager(authenticationManager(http));
        return filter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/login.html").permitAll()
                .mvcMatchers("/verify").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .defaultSuccessUrl("/index.html", true)
                .and()
                .logout()
                .and()
                .csrf().disable();
        http.addFilterAt(verifyFilter(http), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
