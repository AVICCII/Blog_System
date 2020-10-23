package com.aviccii.cc.services;

import com.aviccii.cc.pojo.FriendLink;
import com.aviccii.cc.response.ResponseResult;

/**
 * @author aviccii 2020/10/16
 * @Discrimination
 */
public interface IFriendLinkService {
    ResponseResult addFriendLink(FriendLink friendLink);

    ResponseResult getFriendLink(String friendLinkId);

    ResponseResult listFriendLinks();

    ResponseResult deleteFriendLink(String friendLinkId);

    ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink);

}
