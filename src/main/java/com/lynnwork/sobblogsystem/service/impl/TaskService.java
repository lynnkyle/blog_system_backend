package com.lynnwork.sobblogsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class TaskService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmailVerifyCode(String verifyCode, String emailAddress) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("13788809958@163.com", "Lynn博客系统");
        mimeMessageHelper.setTo(emailAddress);
        mimeMessageHelper.setSubject("Lynn博客系统注册验证码");
        mimeMessageHelper.setText("您的验证码为:" + verifyCode + ",请在10分钟内完成验证。如非本人操作,请忽略此邮件。", false);
        mailSender.send(mimeMessage);
    }
}
