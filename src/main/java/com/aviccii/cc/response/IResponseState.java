package com.aviccii.cc.response;

/**
 * @author aviccii 2020/9/15
 * @Discrimination
 */
public interface IResponseState {
    String getMessage();
    boolean isSuccess();
    int getCode();
    Object getData();
}
