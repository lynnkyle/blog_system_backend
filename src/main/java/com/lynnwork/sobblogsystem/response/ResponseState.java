package com.lynnwork.sobblogsystem.response;

import lombok.Getter;

@Getter
public enum ResponseState {
    SUCCESS(true, 20000, "操作成功"),
    FAILED(false, 40000, "操作失败");
    private boolean isSuccess;
    private int code;
    private String message;

    ResponseState(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}