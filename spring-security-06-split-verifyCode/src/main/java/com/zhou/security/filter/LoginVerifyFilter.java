package com.zhou.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.security.exception.VerifyException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * ClassName: My
 * Package: com.zhou.security.filter
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-14:26
 * @Version: v1.0
 */
public class LoginVerifyFilter extends UsernamePasswordAuthenticationFilter {

    public static final String FORM_VERIFY_CODE_KEY = "verify";

    private String verifyCode = FORM_VERIFY_CODE_KEY;


    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 判断是否为 post 方式请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported:" + request.getMethod());
        }
        // 判断是否为 json 数据
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            // 获取 verity 并判断验证码是否正确
            try {
                Map<String, String> user = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                // 判断验证码
                String verify = user.get(getVerifyCode());
                System.out.println("verify = " + verify);
                String verifyCode = (String) request.getSession().getAttribute("verifyCode");
                if (!ObjectUtils.isEmpty(verifyCode) && !ObjectUtils.isEmpty(verify) && verifyCode.equalsIgnoreCase(verify)) {
                    // 取出 username 和 password
                    String username = user.get(getUsernameParameter());
                    System.out.println("username = " + username);
                    String password = user.get(getPasswordParameter());
                    System.out.println("password = " + password);
                    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                    setDetails(request, authRequest);
                    return this.getAuthenticationManager().authenticate(authRequest);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new VerifyException("验证码错误!");
    }

}