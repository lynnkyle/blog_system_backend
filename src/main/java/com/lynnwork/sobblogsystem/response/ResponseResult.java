package com.lynnwork.sobblogsystem.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult {
    private boolean isSuccess;
    private int code;
    private String message;
    private Object data;

    public ResponseResult(ResponseState state) {
        this.isSuccess = state.isSuccess();
        this.code = state.getCode();
        this.message = state.getMessage();
    }

    public static ResponseResult GET(ResponseState state) {
        return new ResponseResult(state);
    }

    public static ResponseResult SUCCESS() {
        return new ResponseResult(ResponseState.SUCCESS);
    }

    public static ResponseResult SUCCESS(String message) {
        ResponseResult responseResult = new ResponseResult(ResponseState.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult FAILED() {
        return new ResponseResult(ResponseState.FAILED);
    }

    public static ResponseResult FAILED(String message) {
        ResponseResult responseResult = new ResponseResult(ResponseState.FAILED);
        responseResult.setMessage(message);
        return responseResult;
    }

    public ResponseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
