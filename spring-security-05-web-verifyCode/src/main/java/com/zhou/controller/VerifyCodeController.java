package com.zhou.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
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
@Controller
public class VerifyCodeController {


    private final Producer producer;

    @Autowired
    public VerifyCodeController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/verify")
    public void verify(HttpSession session, HttpServletResponse response) throws IOException {
        // 1. 生成验证码
        String verifyCode = producer.createText();
        // 2. 保存验证码到 session 可以存入 redis
        session.setAttribute("verifyCode", verifyCode);
        // 设置返回前端图片类型
        response.setContentType("images/png");
        // 3. 创建图片, 并放到输出流中
        BufferedImage image = producer.createImage(verifyCode);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
    }

}