package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.FriendLinkDao;
import com.aviccii.cc.pojo.FriendLink;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.services.IFriendLinkService;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author aviccii 2020/10/16
 * @Discrimination
 */
@Service
@Transactional
public class FriendLinkServiceImpl extends BaseSerive implements IFriendLinkService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private FriendLinkDao friendLinkDao;

    @Autowired
    private IUserService iUserService;

    /**
     * 添加友情链接
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult addFriendLink(FriendLink friendLink) {

        //判断数据
        String url = friendLink.getUrl();
        if (TextUtils.isEmpty(url)) {
            return ResponseResult.FAILED("链接不可为空");
        }
        String logo = friendLink.getLogo();
        if (TextUtils.isEmpty(logo)) {
            return ResponseResult.FAILED("链接不可为空");
        }
        String name = friendLink.getName();
        if (TextUtils.isEmpty(name)) {
            return ResponseResult.FAILED("链接不可为空");
        }
        //补全数据
        friendLink.setId(idWorker.nextId()+"");
        friendLink.setUpdateTime(new Date());
        friendLink.setCreateTime(new Date());
        //保存数据
        friendLinkDao.save(friendLink);
        //返回结果
        return ResponseResult.SUCCESS("友情链接添加成功");
    }

    @Override
    public ResponseResult getFriendLink(String friendLinkId) {
        FriendLink friendLink = friendLinkDao.findOneById(friendLinkId);
        if (friendLink == null) {
            return ResponseResult.FAILED("该友情链接不存在.");
        }
        ResponseResult success = ResponseResult.SUCCESS("获取成功");
        success.setData(friendLink);
        return success;
    }

    @Override
    public ResponseResult listFriendLinks() {
        //创建条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime","order");
        List<FriendLink> all;
        User user = iUserService.checkUser();
        if (user == null||!Constants.user.ROLE_ADMIN.equals(user.getRole())) {
            //只能获取到正常的category
            all = friendLinkDao.listfriendLinkByState("1");
        }else {
            //查询
            all = friendLinkDao.findAll(sort);
        }
        ResponseResult success = ResponseResult.SUCCESS("获取友情链接列表成功");
        success.setData(all);
        return success;
    }

    @Override
    public ResponseResult deleteFriendLink(String friendLinkId) {
        int result = friendLinkDao.deleteAllById(friendLinkId);
        if (result==0){
            return ResponseResult.FAILED("删除失败");
        }
        return ResponseResult.SUCCESS("删除成功");
    }

    /**
     * 更新内容
     * logo 网站名称 url order
     * @param friendLinkId
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink) {
        FriendLink friendLinkFromDb = friendLinkDao.findOneById(friendLinkId);
        if (friendLinkFromDb == null) {
            return ResponseResult.FAILED("更新失败");
        }
        String logo = friendLink.getLogo();
        if (!TextUtils.isEmpty(logo)) {
            friendLinkFromDb.setLogo(logo);
        }
        String name = friendLink.getName();
        if (!TextUtils.isEmpty(name)) {
            friendLinkFromDb.setName(name);
        }

        String url = friendLink.getUrl();
        if (!TextUtils.isEmpty(url)) {
            friendLinkFromDb.setUrl(url);
        }
        friendLinkFromDb.setOrder(friendLink.getOrder());
        friendLinkFromDb.setUpdateTime(new Date());
        //保存数据
        friendLinkDao.save(friendLinkFromDb);

        return ResponseResult.SUCCESS("更新成功");
    }
}
