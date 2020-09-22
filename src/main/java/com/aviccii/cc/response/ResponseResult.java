package com.aviccii.cc.response;

/**
 * @author aviccii 2020/9/15
 * @Discrimination
 */
public class ResponseResult {
    private String message;
    private boolean success;
    private int code;
    private Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseResult(IResponseState iResponseState) {
        this.message = iResponseState.getMessage();
        this.success = iResponseState.isSuccess();
        this.code = iResponseState.getCode();
        this.data= iResponseState.getData();
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
