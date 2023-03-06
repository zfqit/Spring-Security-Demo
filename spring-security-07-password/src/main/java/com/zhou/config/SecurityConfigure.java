package com.zhou.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ClassName: SecurityConfigure
 * Package: com.zhou.config
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-17:08
 * @Version: v1.0
 */
@Configuration
public class SecurityConfigure {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfigure(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    /**
     * 不推荐使用全局的密码加密方式, 扩展性差
     * @return
     */
    //@Bean
    //public BCryptPasswordEncoder passwordEncoder(){
    //    return new BCryptPasswordEncoder();
    //}


    /**
     * 推荐使用在密码前面加前缀指定加密方式, 扩展性强
     * @return
     */
    //@Bean
    //public UserDetailsService userDetailsService() {
    //    InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
    //    inMemoryUserDetailsManager.createUser(User.builder().username("root").password("{bcrypt}$2a$10$3r0AyI7YaOEv8TWgW1aBBeyQYZkWoC0xco/LU3a1CcsaNzI3Bp5dm").roles("admin").build());
    //    return inMemoryUserDetailsManager;
    //}

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService);
        return auth.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .and()
                .csrf().disable();
        return http.build();
    }
}
