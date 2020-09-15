package com.aviccii.cc.response;

import lombok.Data;

/**
 * @author aviccii 2020/9/15
 * @Discrimination
 */
@Data
public class ResponseResult {
    private String message;
    private boolean success;
    private int code;
    private Object data;

    public ResponseResult(IResponseState iResponseState) {
        this.message = iResponseState.getMessage();
        this.success = iResponseState.isSuccess();
        this.code = iResponseState.getCode();
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(ResponseState.SUCCESS);
    }


    public static ResponseResult SUCCESS(String message){
        ResponseResult success = SUCCESS();
        success.setMessage(message);
        return success;
    }

    public static ResponseResult FAILED() {
        return new ResponseResult(ResponseState.FAILED);
    }

    public static ResponseResult FAILED(String message) {
        ResponseResult failed = FAILED();
        failed.setMessage(message);
        return failed;
    }
}
