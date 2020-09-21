package com.aviccii.cc.controller.admin;

import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/web_size_info")
public class WebSizeInfoApi {

    @GetMapping("/title")
    public ResponseResult getWebSizeTitle(){
        return null;
    }

    @PutMapping("/title")
    public ResponseResult upWebSizeTitle(@RequestParam("title")String title){
        return null;
    }

    @GetMapping("/seo")
    public ResponseResult GETSeoInfo(){
        return null;
    }

    @PutMapping("/seo")
    public ResponseResult putSeoInfo(@RequestParam("keywords")String keywords,
                                     @RequestParam("description")String description){
        return null;
    }

    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount(){
        return null;
    }
}
