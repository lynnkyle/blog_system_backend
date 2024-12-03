package com.lynnwork.sobblogsystem.utils;

import com.lynnwork.sobblogsystem.pojo.User;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

public class ClaimsUtils {
    public static final String ID = "id";
    public static final String USER_NAME = "user_name";
    public static final String ROLE = "role";
    public static final String AVATAR = "avatar";
    public static final String EMAIL = "email";
    public static final String SIGN = "sign";
    public static final String STATE = "state";

    public static Map<String, Object> user2Claims(User user) {
        return new HashMap<String, Object>() {{
            put(ID, user.getId());
            put(USER_NAME, user.getUserName());
            put(ROLE, user.getRole());
            put(AVATAR, user.getAvatar());
            put(EMAIL, user.getEmail());
            put(SIGN, user.getSign());
            put(STATE, user.getState());
        }};
    }

    public static User claims2User(Claims claims) {
        User user = new User();
        user.setId((String) claims.get(ID));
        user.setUserName((String) claims.get(USER_NAME));
        user.setRole((String) claims.get(ROLE));
        user.setAvatar((String) claims.get(AVATAR));
        user.setEmail((String) claims.get(EMAIL));
        user.setSign((String) claims.get(SIGN));
        user.setState((String) claims.get(STATE));
        return user;
    }
}
