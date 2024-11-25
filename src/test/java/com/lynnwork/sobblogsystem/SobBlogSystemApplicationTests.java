package com.lynnwork.sobblogsystem;

import com.lynnwork.sobblogsystem.mapper.DemoMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Calendar;
import java.util.Date;


@Slf4j
@SpringBootTest
class SobBlogSystemApplicationTests {
    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void messageTest() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //1.设置邮件的发送人、收件人、主题
        helper.setFrom("13788809958@163.com", "阳光沙滩博客系统");
        helper.setTo("903586678@qq.com");
        helper.setSubject("The Email with HTML content and Attachment");
        //2.设置邮件的文本内容
        String htmlText = "<h1>Hello, this is a test email</h1>" +
                "<p>This is a <b>HTML</b> content example!</p>" +
                "<p>Here is an <a href='https://www.example.com'>example link</a></p>";
        helper.setText(htmlText, true);
        //3.设置附件
        File file1 = new File("src/main/resources/static/default_avatar.png");
        File file2 = new File("src/main/resources/static/index.html");
        helper.addAttachment("avatar.png", file1);
        helper.addAttachment("avatar.html", file2);
        //4.发送邮件
        mailSender.send(mimeMessage);
    }
}
