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
    ERROR_404(40004, false, "页面丢失",null),
    ERROR_403(40005, false, "权限不够",null),
    ERROR_504(40006, false, "系统繁忙，请稍后再试",null),
    ERROR_505(40003, false, "请求错误，请检查所提交数据",null);


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
