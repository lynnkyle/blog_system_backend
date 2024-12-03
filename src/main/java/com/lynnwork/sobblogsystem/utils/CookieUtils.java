package com.lynnwork.sobblogsystem.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtils {
    private static final int defaultMaxAge = 365 * 24 * 60 * 60;
    private static final String domain = "127.0.0.1";

    public static void setUpCookie(HttpServletResponse response, String name, String value) {
        setUpCookie(response, name, value, defaultMaxAge);
    }

    public static void setUpCookie(HttpServletResponse response, String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookieValue(HttpServletResponse response, String key) {
        setUpCookie(response, key, null, 0);
    }

    public static String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        return null;
    }
}
