package com.aviccii.cc.controller.admin;

import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IWebSizeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/web_size_info")
public class WebSizeInfoAdminApi {

    @Autowired
    private IWebSizeInfoService iWebSizeInfoService;

    @PreAuthorize("@permission.admin()")
    @GetMapping("/title")
    public ResponseResult getWebSizeTitle(){
        return iWebSizeInfoService.getWebSizeTitle();
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/title")
    public ResponseResult upWebSizeTitle(@RequestParam("title")String title){
        return iWebSizeInfoService.putWebSizeTitle(title);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/seo")
    public ResponseResult GETSeoInfo(){
        return iWebSizeInfoService.getSeoInfo();
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/seo")
    public ResponseResult putSeoInfo(@RequestParam("keywords")String keywords,
                                     @RequestParam("description")String description){
        return iWebSizeInfoService.putSeoInfo(keywords,description);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount(){
        return iWebSizeInfoService.getSizeViewCount();
    }
}
