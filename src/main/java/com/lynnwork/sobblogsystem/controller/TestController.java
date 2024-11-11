package com.lynnwork.sobblogsystem.controller;

import com.lynnwork.sobblogsystem.pojo.Demo;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
public class TestController {
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache"); // 缓存1小时（3600秒）
        response.setDateHeader("Expires", 0); // 设置过期时间为1小时后
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        // 验证码存入session
        request.getSession().setAttribute("captcha", specCaptcha.text().toLowerCase());

        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    @GetMapping("/test_img")
    public void test(HttpServletResponse response) {
        response.setContentType("image/png");
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/static/default_avatar.png");
            OutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while (fis.read(bytes) != 0) {
                outputStream.write(bytes);
            }
            fis.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
