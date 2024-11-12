package com.lynnwork.sobblogsystem.utils;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

@Slf4j
public class EmailSender {
    private static final String TAG ="EmailSender";
    private static Session session;

    private MimeMessage msg;

}
