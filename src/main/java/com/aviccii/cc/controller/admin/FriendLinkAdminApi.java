package com.aviccii.cc.controller.admin;

import com.aviccii.cc.pojo.FriendLink;
import com.aviccii.cc.services.IFriendLinkService;
import com.aviccii.cc.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/friend_link")
public class FriendLinkAdminApi {

    @Autowired
    private IFriendLinkService iFriendLinkService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addFriendsLink(@RequestBody FriendLink friendLink){
        return iFriendLinkService.addFriendLink(friendLink);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{friendLinkId}")
    public ResponseResult deleteFriendLink(@PathVariable("friendLinkId")String friendLinkId){
        return iFriendLinkService.deleteFriendLink(friendLinkId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/{friendLinkId}")
    public ResponseResult uploadFriendLink(@PathVariable("friendLinkId")String friendLinkId,
                                           @RequestBody FriendLink friendLink){
        return iFriendLinkService.updateFriendLink(friendLinkId,friendLink);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/{friendLinkId}")
    public ResponseResult getFriendLink(@PathVariable("friendLinkId")String friendLinkId){
        return iFriendLinkService.getFriendLink(friendLinkId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listFriendLinks(@PathVariable("page")int page,@PathVariable("size")int size){
        return iFriendLinkService.listFriendLinks(page,size);
    }
}
