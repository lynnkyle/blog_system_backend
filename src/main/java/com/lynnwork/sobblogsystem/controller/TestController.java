package com.lynnwork.sobblogsystem.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;

@Slf4j
@Controller
public class TestController {
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "public,max-age=3600");
        response.setDateHeader("Expires", System.currentTimeMillis() + 3600000);
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        request.getSession().setAttribute("captcha", specCaptcha.text().toLowerCase());
        specCaptcha.out(response.getOutputStream());
        String content = specCaptcha.text().toLowerCase();
        request.getSession().setAttribute("captcha", content);
        log.info("content:{}",content);
    }

    @GetMapping("/test_img")
    public void test(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long lastModified = 1638764400000L;
        long ifModifiedSince = request.getDateHeader("if-modified-since");
        if (ifModifiedSince >= lastModified) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        } else {
            response.setContentType("image/gif");
            response.setHeader("pragma", "public");
            response.setHeader("Cache-Control", "public,max-age=3600");
            response.setDateHeader("Expires", 3600000);
            response.setDateHeader("Last-Modified", lastModified);
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
    }
}
