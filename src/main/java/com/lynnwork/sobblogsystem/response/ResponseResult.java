package com.lynnwork.sobblogsystem.response;

import com.baomidou.mybatisplus.extension.api.R;
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

    public static ResponseResult JOIN_IN_SUCCESS() {
        return new ResponseResult(ResponseState.JOIN_IN_SUCCESS);
    }

    public static ResponseResult ERROR_403() {
        return new ResponseResult(ResponseState.ERROR_404);
    }

    public static ResponseResult ERROR_404() {
        return new ResponseResult(ResponseState.ERROR_404);
    }

    public static ResponseResult ERROR_504() {
        return new ResponseResult(ResponseState.ERROR_504);
    }

    public static ResponseResult ERROR_505() {
        return new ResponseResult(ResponseState.ERROR_505);
    }

    public static ResponseResult PERMISSION_DENY() {
        return new ResponseResult(ResponseState.PERMISSION_DENY);
    }

    public static ResponseResult ACCOUNT_NOT_LOGIN() {
        return new ResponseResult(ResponseState.ACCOUNT_NOT_LOGIN);
    }

    public static ResponseResult ACCOUNT_DENY() {
        return new ResponseResult(ResponseState.ACCOUNT_DENY);
    }

    public ResponseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
