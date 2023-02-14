package com.zhou.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义 filter 用来替换 formLogin 的 filter
 *
 * @author zhoufuqi
 * @date 2023/2/14
 */
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("---------------------------------------");
        // 判断是否为 post 请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 判断是否为 json 数据
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            // 取出 username 和 password
            try {
                Map<String, String> user = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = user.get(getUsernameParameter());
                System.out.println("username = " + username);
                String password = user.get(getPasswordParameter());
                System.out.println("password = " + password);
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.attemptAuthentication(request, response);
    }

}
