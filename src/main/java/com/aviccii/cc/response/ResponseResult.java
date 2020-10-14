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

    public static ResponseResult GET(ResponseState state){
        return new ResponseResult(state);
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(ResponseState.SUCCESS);
    }

    public static ResponseResult ACCOUNT_NOT_LOGIN(){
        return new ResponseResult(ResponseState.ACCOUNT_NOT_LOGIN);
    }

    public static ResponseResult ERROR_404(){
        return new ResponseResult(ResponseState.ERROR_404);
    }

    public static ResponseResult ERROR_403(){
        return new ResponseResult(ResponseState.ERROR_403);
    }

    public static ResponseResult ERROR_504(){
        return new ResponseResult(ResponseState.ERROR_504);
    }

    public static ResponseResult ERROR_505(){
        return new ResponseResult(ResponseState.ERROR_505);
    }

    public static ResponseResult PERMISSION_FORBID(){
        return new ResponseResult(ResponseState.PERMISSION_FORBID);
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
