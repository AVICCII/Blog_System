package com.aviccii.cc.controller.portal;

import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ICategoryService;
import com.aviccii.cc.services.IFriendLinkService;
import com.aviccii.cc.services.ILoopService;
import com.aviccii.cc.services.IWebSizeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * 统计访问量，每个页面都统计一次，page view
     * 直接增加一个访问量，可以刷量
     * 根据ip进行一些过滤，可以继承第三方的统计工具
     *
     * 递增的统计
     * 统计信息，通过redis统计，数据也会保存到mysql里
     * 不会每次都更新到mysql里，当用户去获取访问量的时候，会更新一次
     * 平时的调用只会增加redis里的访问量
     *
     * redis时机：每个页面访问的时候，如果不存在从mysql中获取数据，写道redis里
     *
     * mysql时机：用户获取网站总访问量的时候，我们就读取一下redis的，并且更新到mysql中
     * 如果redis里没有，那就读取mysql写到redis里
     *
     */
    @PutMapping("/view_count")
    public void updateViewCount(){
        iWebSizeInfoService.updateViewCount();
    }
}
