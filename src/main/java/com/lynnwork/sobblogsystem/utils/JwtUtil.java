package com.lynnwork.sobblogsystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    // 盐值
    private static final String key = "67378bf38348f4aa958c1144252e7b2b";

    private static long ttl = Constants.TimeValueInMillions.HOUR_2;

    public static long getTtl() {
        return ttl;
    }

    public static void setTtl(long ttl) {
        JwtUtil.ttl = ttl;
    }

    public static String createToken(Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);
        if (claims != null) {
            builder.setClaims(claims);
        }
        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    public static String createToken(Map<String, Object> claims, long ttl) {
        JwtUtil.ttl = ttl;
        return createToken(claims);
    }

    public static String createRefreshToken(String userId, long ttl) {
        long nowMillis = System.currentTimeMillis();
        Date date = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(userId)
                .setIssuedAt(date)
                .signWith(SignatureAlgorithm.HS256, key);
        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}
