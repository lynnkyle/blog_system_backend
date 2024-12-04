package com.lynnwork.sobblogsystem.response;

import lombok.Getter;

@Getter
public enum ResponseState {
    SUCCESS(true, 20000, "操作成功"),
    LOGIN_SUCCESS(true, 20001, "登录成功"),
    JOIN_IN_SUCCESS(true, 20002, "注册成功"),
    FAILED(false, 40000, "操作失败"),
    GET_RESOURCE_FAILED(false, 40001, "获取资源失败"),
    ACCOUNT_NOT_LOGIN(false, 40002, "账号未登录"),
    PERMISSION_DENY(false, 40003, "无权访问"),
    LOGIN_FAILED(false,49999,"登陆失败");

    private boolean isSuccess;
    private int code;
    private String message;

    ResponseState(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}