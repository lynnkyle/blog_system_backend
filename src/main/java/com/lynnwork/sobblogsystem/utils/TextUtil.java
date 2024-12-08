package com.lynnwork.sobblogsystem.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    public static boolean isEmpty(String text) {
        if (text == null || text.trim().length() == 0) return true;
        return false;
    }

    public static boolean isEmailAddressOk(String email) {
        if (TextUtil.isEmpty(email)) return false;
        String regExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
