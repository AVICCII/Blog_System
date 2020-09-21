package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.SettingsDao;
import com.aviccii.cc.dao.UserDao;
import com.aviccii.cc.pojo.Setting;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 * 事务管理是应用系统开发中必不可少的一部分。Spring 为事务管理提供了丰富的功能支持。Spring 事务管理分为编程式和声明式的两种方式。
 * 编程式事务指的是通过编码方式实现事务；声明式事务基于 AOP,将具体业务逻辑与事务处理解耦。声明式事务管理使业务代码逻辑不受污染,
 * 因此在实际使用中声明式事务用的比较多。声明式事务有两种方式，一种是在配置文件（xml）中做相关的事务规则声明，
 * 另一种是基于 @Transactional 注解的方式。本文将着重介绍基于 @Transactional 注解的事务管理。
 *
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SettingsDao settingsDao;

    @Override
    public ResponseResult initManagerAccount(User user, HttpServletRequest request) {
        //查询是否初始化
        Setting managerAccountState = settingsDao.findOneByKey(Constants.settings.HAS_MANAGER_ACCOUNT_INIT_STATE);
        if (managerAccountState!=null){
            return ResponseResult.FAILED("管理员账号已经初始化了");
        }
        //检查数据
        if (TextUtils.isEmpty(user.getUser_name())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (TextUtils.isEmpty(user.getPassword())){
            return ResponseResult.FAILED("密码不能为空");
        }

        if (TextUtils.isEmpty(user.getEmail())){
            return ResponseResult.FAILED("邮箱不能为空");
        }
        //补充数据
        user.setId(String.valueOf(idWorker.nextId()));
        user.setRole(Constants.user.ROLE_ADMIN);
        user.setAvatar(Constants.user.DEFAULT_AVATAR);
        user.setState(Constants.user.DEFAULT_STATE);
        String remoteAddr = request.getRemoteAddr();
        String localAddr = request.getLocalAddr();
        user.setLogin_ip(remoteAddr);
        user.setReg_ip(remoteAddr);
        user.setCreate_time(new Date());
        user.setUpdate_time(new Date());
        //保存到数据库中
        userDao.save(user);
        //更新已经添加的标记
        Setting setting = new Setting();
        setting.setId(idWorker.nextId()+"");
        setting.setKey(Constants.settings.HAS_MANAGER_ACCOUNT_INIT_STATE);
        setting.setCreate_time(new Date());
        setting.setUpdate_time(new Date());
        setting.setValue("1");
        settingsDao.save(setting);
        return ResponseResult.SUCCESS("初始化成功");
    }

}
