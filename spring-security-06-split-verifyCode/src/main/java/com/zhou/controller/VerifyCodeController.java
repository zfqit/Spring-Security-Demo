package com.zhou.controller;

import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ClassName: TestController
 * Package: com.zhou.controller
 * Description:
 *
 * @Author: zhoufuqi
 * @Create：2023/2/15-13:58
 * @Version: v1.0
 */
@RestController
public class VerifyCodeController {


    private final Producer producer;

    @Autowired
    public VerifyCodeController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/verify")
    public String verify(HttpSession session, HttpServletResponse response) throws IOException {
        // 1. 生成验证码
        String verifyCode = producer.createText();
        // 2. 保存验证码到 session 可以存入 redis
        session.setAttribute("verifyCode", verifyCode);
        // 3. 创建图片,
        BufferedImage image = producer.createImage(verifyCode);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image, "png", fos);
        //3.生成 base64
        return Base64.encodeBase64String(fos.toByteArray());
    }

    @PostMapping("/test")
    public String test() {
        System.out.println("test");
        return "test";
    }

}