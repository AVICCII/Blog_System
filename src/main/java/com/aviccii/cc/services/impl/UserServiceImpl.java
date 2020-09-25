package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.SettingsDao;
import com.aviccii.cc.dao.UserDao;
import com.aviccii.cc.pojo.Setting;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.response.ResponseState;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.*;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import static com.aviccii.cc.controller.user.UserApi.captcha_font_types;
import static com.aviccii.cc.utils.Constants.user.ROLE_NORMAL;

/**
 * @author aviccii 2020/9/21
 * @Discrimination 事务管理是应用系统开发中必不可少的一部分。Spring 为事务管理提供了丰富的功能支持。Spring 事务管理分为编程式和声明式的两种方式。
 * 编程式事务指的是通过编码方式实现事务；声明式事务基于 AOP,将具体业务逻辑与事务处理解耦。声明式事务管理使业务代码逻辑不受污染,
 * 因此在实际使用中声明式事务用的比较多。声明式事务有两种方式，一种是在配置文件（xml）中做相关的事务规则声明，
 * 另一种是基于 @Transactional 注解的方式。
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        if (managerAccountState != null) {
            return ResponseResult.FAILED("管理员账号已经初始化了");
        }
        //检查数据
        if (TextUtils.isEmpty(user.getUserName())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }

        if (TextUtils.isEmpty(user.getEmail())) {
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
        //对密码进行加密
        String password = user.getPassword();
        //加密码
        String encode = bCryptPasswordEncoder.encode(password);
        user.setPassword(encode);
        //保存到数据库中
        userDao.save(user);
        //更新已经添加的标记
        Setting setting = new Setting();
        setting.setId(idWorker.nextId() + "");
        setting.setKey(Constants.settings.HAS_MANAGER_ACCOUNT_INIT_STATE);
        setting.setCreate_time(new Date());
        setting.setUpdate_time(new Date());
        setting.setValue("1");
        settingsDao.save(setting);
        return ResponseResult.SUCCESS("初始化成功");
    }

    @Autowired
    private Random random;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void createCaptcha(HttpServletResponse response, String captchaKey) throws IOException {
        if (TextUtils.isEmpty(captchaKey) || captchaKey.length() < 13) {
            return;
        }
        long key = 0L;
        try {
            key = Long.parseLong(captchaKey);
            //处理
        } catch (Exception e) {
            return;
        }

        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        int captchaType = random.nextInt();
        Captcha targetCaptcha = null;
        if (captchaType == 0) {
            targetCaptcha = new SpecCaptcha(200, 60, 5);

        } else if (captchaType == 1) {
            //gif类型
            targetCaptcha = new GifCaptcha(200, 60);
        } else {
            //算术类型
            targetCaptcha = new ArithmeticCaptcha(200, 60);
            targetCaptcha.setLen(2);
            targetCaptcha.text();
        }


        int i = random.nextInt(captcha_font_types.length);
        log.info("captcha font type index == >" + i);
        try {
            targetCaptcha.setFont(captcha_font_types[i]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        targetCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        String content = targetCaptcha.text().toLowerCase();
        log.info("captcha content == >" + content);
        //保存到redis
        redisUtil.set(Constants.user.KEY_CAPTCHA_CONTENT + "key", content, 60 * 10);
        targetCaptcha.out(response.getOutputStream());
    }

    @Autowired
    private TaskService taskService;

    /**
     * 发送验证码
     * 根据场景类型判断
     * @param request
     * @param emailAddress
     * @return
     */
    @Override
    public ResponseResult sendemail(String type,HttpServletRequest request, String emailAddress) {
        //根据类型查询邮箱是否存在
        if (emailAddress == null) {
            return ResponseResult.FAILED("邮箱地址不可以为空");
        }

        if("register".equals(type)||"update".equals(type)){
            User userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail != null) {
                return ResponseResult.FAILED("该邮箱已经注册");
            }
        }else if ("forget".equals(type)){
            User userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail == null) {
                return ResponseResult.FAILED("该邮箱未注册");
            }
        }


        //1.防止暴力发送  同一邮箱30秒间隔，同一ip1小时最多10次

        String remoteAddr = request.getRemoteAddr();
        log.info("sendEmail == > ip == > " + remoteAddr);
        if (remoteAddr != null) {
            remoteAddr = remoteAddr.replaceAll(":", "_");
        }
        //拿出来如果没有，那就过，如果有则判断次数
        log.info("Constants.user.KEY_EMAIL_SEND_IP+remoteAddr === >" + Constants.user.KEY_EMAIL_SEND_IP + remoteAddr);
        Integer ipSendTime = (Integer) redisUtil.get(Constants.user.KEY_EMAIL_SEND_IP + remoteAddr);
        if (ipSendTime != null) {
            if (ipSendTime > 10) {
                return ResponseResult.FAILED("请不要太频繁发送");
            }
        }
        Object addressSendTime = redisUtil.get(Constants.user.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        if (addressSendTime != null) {
            return ResponseResult.FAILED("请不要太频繁发送");
        }

        //2.检查邮箱地址是否正确

        boolean isEmailFormatOk = TextUtils.isEmailAddressOk(emailAddress);
        if (!isEmailFormatOk) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
        }
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += 100000;
        }
        log.info("sendEmail code == >" + code);
        //3.发送验证码
        try {
            taskService.sendEmailVerifyCode(String.valueOf(code),emailAddress);
        } catch (Exception e) {
            return ResponseResult.FAILED("验证码发送失败，请稍后重试");
        }
        //4.做记录
        if (ipSendTime == null) {
            ipSendTime = 0;
        }
        ipSendTime++;
        //1个小时有效期
        redisUtil.set(Constants.user.KEY_EMAIL_SEND_IP + remoteAddr, ipSendTime, 60 * 60);
        redisUtil.set(Constants.user.KEY_EMAIL_SEND_ADDRESS + emailAddress, 1, 30);
        //保存code,10分钟内有效
        redisUtil.set(Constants.user.KEY_EMAIL_CODE_CONTENT + emailAddress, code, 60 * 10);
        return ResponseResult.SUCCESS("发送成功");
    }

    @Override
    public ResponseResult register(User user, String emailCode, String captchaCode, String captchaKey, HttpServletRequest request) {
        //第一步：检查当前用户名是否已经注册
        String userName = user.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.FAILED("用户名不为空");
        }
        User userByName = userDao.findOneByUserName(userName);
        if (userByName != null) {
            return ResponseResult.SUCCESS("用户名已注册");
        }
        //第二步：检查邮箱格式是否正确
        String email = user.getEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱地址不为空");
        }
        if (!TextUtils.isEmailAddressOk(email)) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
        }
        //第三步：检查当前邮箱是否已经注册
        User userByEmail = userDao.findOneByEmail(email);
        if (userByEmail!=null) {
            return ResponseResult.FAILED("该邮箱地址已注册");

        }
        //第四步：检查邮箱验证码是否正确
        String emailVerifyCode = String.valueOf(redisUtil.get(Constants.user.KEY_EMAIL_CODE_CONTENT + email)) ;
        if (TextUtils.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("该邮箱地址已过期");
        }
        if (!emailVerifyCode.equals(emailCode)) {
            return ResponseResult.FAILED("邮箱验证码不正确");
        }else {
            //正确,干掉redis里的内容
            redisUtil.del(Constants.user.KEY_EMAIL_CODE_CONTENT + email);
        }
        //第五步：检查图灵验证码是否正确
        String captchaVerifyCode = (String) redisUtil.get(Constants.user.KEY_CAPTCHA_CONTENT + captchaKey);
        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILED("验证码已过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)){
            return ResponseResult.FAILED("验证码不正确");
        }else {
            redisUtil.del(Constants.user.KEY_CAPTCHA_CONTENT+captchaKey);
        }
        //达到可以注册的条件
        //对密码加密
        String password = user.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //补全数据
        String ipAddress = request.getRemoteAddr();
        user.setReg_ip(ipAddress);
        user.setLogin_ip(ipAddress);
        user.setUpdate_time(new Date());
        user.setCreate_time(new Date());
        user.setAvatar(Constants.user.DEFAULT_AVATAR);
        user.setRole(ROLE_NORMAL);
        user.setState("1");
        user.setId(idWorker.nextId()+"");
        //保存到数据库中
        userDao.save(user);
        //返回结果
        return ResponseResult.GET(ResponseState.JOIN_IN_SUCCESS);
    }

}
