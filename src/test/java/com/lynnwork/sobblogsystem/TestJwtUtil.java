package com.lynnwork.sobblogsystem;

import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class TestJwtUtil {
    public static void main(String[] args) {
//        String jwtKeyMd5Str = DigestUtils.md5DigestAsHex("sob_blog_system_".getBytes());
//        System.out.println("jwtKeyMd5Str:" + jwtKeyMd5Str);
//        Map<String, Object> claims = new HashMap<>();
//        User user = new User();
//        user.setUserName("lzy");
//        user.setPassword("123456");
//        user.setEmail("lzy@gmail.com");
//        claims.put("user", user);
//        String token = JwtUtil.createToken(claims);
//        System.out.println(token);
//        Claims claim = JwtUtil.parseToken(token);
//        System.out.println(claim);
        String encodePassword = DigestUtils.md5DigestAsHex("hello world".getBytes());
        System.out.println(encodePassword);
    }
}
