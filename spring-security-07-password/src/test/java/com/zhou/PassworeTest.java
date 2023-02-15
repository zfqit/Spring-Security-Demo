package com.zhou;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * ClassName: PassworeTest
 * Package: com.zhou
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-17:29
 * @Version: v1.0
 */
public class PassworeTest {

    @Test
    public void password() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123"));
    }
}
