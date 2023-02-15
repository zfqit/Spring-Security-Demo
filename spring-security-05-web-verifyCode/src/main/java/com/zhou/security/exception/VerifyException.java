package com.zhou.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * ClassName: My
 * Package: com.zhou.security.exception
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-14:37
 * @Version: v1.0
 */
public class VerifyException extends AuthenticationException {

    public VerifyException(String msg) {
        super(msg);
    }

    public VerifyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
