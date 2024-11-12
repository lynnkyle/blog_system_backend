package com.lynnwork.sobblogsystem;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
        //$2a$10$r4n1.4SxQT7WmsAJjFoBJekQfHUypMOypoYSC/pNqzYYuWRPf5v22
        //$2a$10$YiF/AmlBpJf8rNAW2bU4AuZyju5slvaiPLAOg6dwAwHhyMwssKo4K
        //$2a$10$oPbf1WmRb/82XBNPQrHsmeJI/qSMSusdIDWJ1gMa/4/Cp34yinD2O
        System.out.println("encode ==>" + encode);
        String originPassword = "123456";
        boolean matches = passwordEncoder.matches(originPassword, "$2a$10$r4n1.4SxQT7WmsAJjFoBJekQfHUypMOypoYSC/NqzYYuRPf5v22");
        System.out.println("isMatch =>" + matches);
    }
}
