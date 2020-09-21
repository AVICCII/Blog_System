package com.aviccii.cc.controller.portal;

import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/portal/web_size_info")
public class WebSizeInfoApi {

    @GetMapping("/categories")
    public ResponseResult getCategories(){
        return null;
    }

    @GetMapping("/title")
    public ResponseResult getWebSizeTitle(){
        return null;
    }

    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount(){
         return null;
    }

    @GetMapping("/seo")
    public ResponseResult getWebSizeSeoInfo(){
        return null;
    }

    @GetMapping("/loop")
    public ResponseResult getLoops(){
        return null;
    }

    @GetMapping("/friend_link")
    public ResponseResult getLinks(){
        return null;
    }
}
