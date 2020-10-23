package com.aviccii.cc.services;

import com.aviccii.cc.pojo.Looper;
import com.aviccii.cc.response.ResponseResult;

/**
 * @author aviccii 2020/10/20
 * @Discrimination
 */
public interface ILoopService {
    ResponseResult addLoop(Looper looper);

    ResponseResult getLoop(String loopId);

    ResponseResult listLooopers();

    ResponseResult updateLoop(String loopId, Looper looper);

    ResponseResult deleteLoop(String loopId);
}
