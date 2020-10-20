package com.aviccii.cc.services.impl;

import com.aviccii.cc.utils.Constants;

/**
 * @author aviccii 2020/10/20
 * @Discrimination
 */
public class BaseSerive {
    int checkPage(int page){
        //参数检查
        if(page< Constants.Page.DEFAULT_PAGE){
            page=Constants.Page.DEFAULT_PAGE;
        }
    return page;
    }

    int checkSize(int size){
        //参数检查
        if (size<Constants.Page.DEFAULT_SIZE){
            size=Constants.Page.DEFAULT_SIZE;
        }
        return size;
    }



}
