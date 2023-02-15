package com.zhou.security.filter;

import com.zhou.security.exception.VerifyException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: My
 * Package: com.zhou.security.filter
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-14:26
 * @Version: v1.0
 */
public class VerifyFilter extends UsernamePasswordAuthenticationFilter {

    public static final String FORM_VERIFY_CODE_KEY = "password";

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
        // 获取 verity 并判断验证码是否正确
        String verifyCode = request.getParameter(getVerifyCode());
        String verify = (String) request.getSession().getAttribute("verifyCode");
        if (!ObjectUtils.isEmpty(verifyCode) && !ObjectUtils.isEmpty(verify) && verifyCode.equalsIgnoreCase(verify)) {
            // 验证正确后直接走账号验证流程
            return super.attemptAuthentication(request, response);
        }
        throw new VerifyException("验证码错误!");
    }

}