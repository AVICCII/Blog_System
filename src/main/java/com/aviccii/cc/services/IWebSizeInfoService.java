package com.aviccii.cc.services;

import com.aviccii.cc.response.ResponseResult;

/**
 * @author aviccii 2020/10/21
 * @Discrimination
 */
public interface IWebSizeInfoService {
    ResponseResult getWebSizeTitle();

    ResponseResult putWebSizeTitle(String title);

    ResponseResult getSeoInfo();

    ResponseResult putSeoInfo(String keywords, String description);

    ResponseResult getSizeViewCount();

    void updateViewCount();
}
