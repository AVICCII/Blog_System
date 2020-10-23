package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.LooperDao;
import com.aviccii.cc.pojo.Looper;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ILoopService;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

/**
 * @author aviccii 2020/10/20
 * @Discrimination
 */
@Slf4j
@Service
@Transactional
public class LoopService extends BaseSerive implements ILoopService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private LooperDao looperDao;

    @Override
    public ResponseResult addLoop(Looper looper) {
        //检查数据
        String title = looper.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("标题不可为空");
        }
        String imageUrl = looper.getImageUrl();
        if (TextUtils.isEmpty(imageUrl)) {
            return ResponseResult.FAILED("图片不可为空");
        }
        String targetUrl = looper.getTargetUrl();
        if (TextUtils.isEmpty(targetUrl)) {
            return ResponseResult.FAILED("跳转链接不可为空");
        }

        //补充数据
        looper.setId(idWorker.nextId()+"");
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());
        //保存数据
        looperDao.save(looper);
        return ResponseResult.SUCCESS("轮播图添加成功");
    }

    @Override
    public ResponseResult getLoop(String loopId) {
        Looper loop = looperDao.findOneById(loopId);
        if (loop == null) {
            return ResponseResult.FAILED("轮播图不存在");
        }
        ResponseResult success = ResponseResult.SUCCESS("轮播图获取成功");
        success.setData(loop);
        return success;
    }

    @Autowired
    private IUserService iUserService;

    @Override
    public ResponseResult listLooopers() {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        User user = iUserService.checkUser();
        List<Looper> all;
        if (user == null||!Constants.user.ROLE_ADMIN.equals(user.getRole())) {
            //只能获取到正常的category
            all = looperDao.listLooperByState("1");
        }else {
            //查询
            all = looperDao.findAll(sort);
        }
        ResponseResult success = ResponseResult.SUCCESS("获取轮播图列表成功");
        success.setData(all);
        return success;
    }

    @Override
    public ResponseResult updateLoop(String loopId, Looper looper) {
        //找出来
        Looper loopfromDb = looperDao.findOneById(loopId);
        if (loopfromDb == null) {
            return ResponseResult.FAILED("轮播图不存在");
        }
        //不可以为空的，要判空
        String title = looper.getTitle();
        if (!TextUtils.isEmpty(title)) {
            loopfromDb.setTitle(title);
        }
        String targetUrl = looper.getTargetUrl();
        if (!TextUtils.isEmpty(targetUrl)) {
            loopfromDb.setTargetUrl(targetUrl);
        }
        String imageUrl = looper.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            loopfromDb.setImageUrl(imageUrl);
        }
        String state = looper.getState();
        if (!TextUtils.isEmpty(state)) {
            loopfromDb.setState(state);
        }
        loopfromDb.setOrder(looper.getOrder());
        loopfromDb.setUpdateTime(new Date());
        //可以为空的直接设置
        //保存结果
        looperDao.save(loopfromDb);
        return ResponseResult.SUCCESS("轮播图更新成功");
    }

    @Override
    public ResponseResult deleteLoop(String loopId) {
        looperDao.deleteById(loopId);
        return ResponseResult.SUCCESS("删除成功");
    }
}
