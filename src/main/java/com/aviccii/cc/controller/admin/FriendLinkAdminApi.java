package com.aviccii.cc.controller.admin;

import com.aviccii.cc.pojo.FriendLink;
import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/friend_link")
public class FriendLinkAdminApi {
    @PostMapping
    public ResponseResult addFriendsLink(@RequestBody FriendLink friendLink){
        return null;
    }

    @DeleteMapping("/{friendLinkId}")
    public ResponseResult deleteFriendLink(@PathVariable("friendLinkId")String friendLinkId){
        return null;
    }

    @PutMapping("/{friendLinkId}")
    public ResponseResult uploadFriendLink(@PathVariable("friendLinkId")String friendLinkId){
        return null;
    }

    @GetMapping("/{friendLinkId}")
    public ResponseResult getFriendLink(@PathVariable("friendLinkId")String friendLinkId){
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listFriendLinks(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }
}
