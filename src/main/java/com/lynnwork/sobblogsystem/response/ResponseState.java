package com.lynnwork.sobblogsystem.response;

import lombok.Getter;

@Getter
public enum ResponseState {
    SUCCESS(true, 20000, "操作成功"),
    LOGIN_SUCCESS(true, 20001, "登录成功"),
    JOIN_IN_SUCCESS(true, 20002, "注册成功"),
    FAILED(false, 40000, "操作失败"),
    ERROR_403(false, 40003, "权限不足"),
    ERROR_404(false, 40004, "页面丢失"),
    ERROR_504(false, 40005, "系统繁忙，请稍后重试"),
    ERROR_505(false, 40006, "请求错误，请检查所提交数据"),
    PERMISSION_DENY(false, 49996, "无权访问"),
    ACCOUNT_NOT_LOGIN(false, 49997, "账号未登录"),
    LOGIN_FAILED(false, 49998, "登录失败"),
    ACCOUNT_DENY(false, 49999, "账号被禁止");

    private boolean isSuccess;
    private int code;
    private String message;

    ResponseState(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}