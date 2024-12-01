package com.lynnwork.sobblogsystem.utils;

public interface Constants {
    interface User {
        String ROLE_ADMIN = "role_admin";
        String DEFAULT_AVATAR = "https://cdn.sunofbeaches.com/images/default_avatar.png";
        String DEFAULT_STATE = "1";
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        String KEY_EMAIL_CODE_CONTENT = "key_email_code_content_";
        String KEY_EMAIL_SEND_IP = "key_email_send_ip_";
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address_";
        String KEY_TOKEN_CONTENT = "key_token_content_";
        String KEY_COOKIE_TOKEN = "sob_blog_token";
    }

    interface Setting {
        String ADMIN_ACCOUNT_INIT_STATE = "admin_account_init_state";
    }

    interface TimeValueInSecond {
        int MIN = 60;
        int HOUR = 60 * MIN;
        int HOUR_2 = HOUR * 2;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
        int YEAR = 365 * DAY;
    }
}
