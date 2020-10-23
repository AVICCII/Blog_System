package com.aviccii.cc.controller.portal;

import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ICategoryService;
import com.aviccii.cc.services.IFriendLinkService;
import com.aviccii.cc.services.ILoopService;
import com.aviccii.cc.services.IWebSizeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/portal/web_size_info")
public class WebSizeInfoPortalApi {

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IFriendLinkService iFriendLinkService;

    @Autowired
    private ILoopService iLoopService;

    @Autowired
    private IWebSizeInfoService iWebSizeInfoService;

    @GetMapping("/categories")
    public ResponseResult getCategories(){
        return iCategoryService.listCategories();
    }

    @GetMapping("/title")
    public ResponseResult getWebSizeTitle(){
        return iWebSizeInfoService.getWebSizeTitle();
    }

    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount(){
         return iWebSizeInfoService.getSizeViewCount();
    }

    @GetMapping("/seo")
    public ResponseResult getWebSizeSeoInfo(){
        return iWebSizeInfoService.getSeoInfo();
    }

    @GetMapping("/loop")
    public ResponseResult getLoops(){
        return iLoopService.listLooopers();
    }

    @GetMapping("/friend_link")
    public ResponseResult getLinks(){
        return iFriendLinkService.listFriendLinks();
    }
}
