package com.aviccii.cc.response;

/**
 * @author aviccii 2020/9/15
 * @Discrimination
 */
public enum ResponseState implements IResponseState {
    SUCCESS(10000, true, "操作成功",null),
    JOIN_IN_SUCCESS(20002, true, "注册成功",null),
    FAILED(20000, false, "操作失败",null),
    ACCOUNT_NOT_LOGIN(40002, false, "账号未登录",null),
    PERMISSION_FORBID(40003, false, "权限不够",null),
    PARAMS_ILL(30000, false, "参数错误",null),
    PERMISSION_DENIED(40000, false, "权限不够",null),
    NOT_LOGIN(50000, false, "账号未登录",null),
    LOGIN_SUCCESS(60000,true, "登录成功",null);


    int code;
    boolean isSuccess;
    String message;
    Object data;

    ResponseState(int code, boolean isSuccess, String message,Object data) {
        this.code = code;
        this.isSuccess = isSuccess;
        this.message = message;
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Object getData() {
        return data;
    }

}
