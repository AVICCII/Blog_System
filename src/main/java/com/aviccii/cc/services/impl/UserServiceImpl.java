package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.RefreshTokenDao;
import com.aviccii.cc.dao.SettingsDao;
import com.aviccii.cc.dao.UserDao;
import com.aviccii.cc.pojo.RefreshToken;
import com.aviccii.cc.pojo.Setting;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.response.ResponseState;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.*;
import com.google.gson.Gson;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static com.aviccii.cc.controller.user.UserApi.captcha_font_types;
import static com.aviccii.cc.utils.Constants.user.*;

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

    @Autowired
    private RefreshTokenDao refreshTokenDao;

    @Autowired
    private Gson gson;

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
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
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
        redisUtil.set(Constants.user.KEY_CAPTCHA_CONTENT + captchaKey, content, 60 * 10);
        targetCaptcha.out(response.getOutputStream());
    }

    @Autowired
    private TaskService taskService;

    /**
     * 发送验证码
     * 根据场景类型判断
     *
     * @param request
     * @param emailAddress
     * @return
     */
    @Override
    public ResponseResult sendemail(String type, HttpServletRequest request, String emailAddress) {
        //根据类型查询邮箱是否存在
        if (emailAddress == null) {
            return ResponseResult.FAILED("邮箱地址不可以为空");
        }

        if ("register".equals(type) || "update".equals(type)) {
            User userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail != null) {
                return ResponseResult.FAILED("该邮箱已经注册");
            }
        } else if ("forget".equals(type)) {
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
            taskService.sendEmailVerifyCode(String.valueOf(code), emailAddress);
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
        redisUtil.set(Constants.user.KEY_EMAIL_SEND_ADDRESS + emailAddress, "true", 30);
        //保存code,10分钟内有效
        redisUtil.set(Constants.user.KEY_EMAIL_CODE_CONTENT + emailAddress, String.valueOf(code), 60 * 10);
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
        if (userByEmail != null) {
            return ResponseResult.FAILED("该邮箱地址已注册");

        }
        //第四步：检查邮箱验证码是否正确
        String emailVerifyCode = String.valueOf(redisUtil.get(Constants.user.KEY_EMAIL_CODE_CONTENT + email));
        if (TextUtils.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("该邮箱地址已过期");
        }
        if (!emailVerifyCode.equals(emailCode)) {
            return ResponseResult.FAILED("邮箱验证码不正确");
        } else {
            //正确,干掉redis里的内容
            redisUtil.del(Constants.user.KEY_EMAIL_CODE_CONTENT + email);
        }
        //第五步：检查图灵验证码是否正确
        System.out.println(" 人类验证码 ======>" + redisUtil.get(Constants.user.KEY_CAPTCHA_CONTENT + captchaKey));
        String captchaVerifyCode = String.valueOf(redisUtil.get(Constants.user.KEY_CAPTCHA_CONTENT + captchaKey));
        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILED("人类验证码已过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILED("验证码不正确");
        } else {
            redisUtil.del(Constants.user.KEY_CAPTCHA_CONTENT + captchaKey);
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
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setAvatar(Constants.user.DEFAULT_AVATAR);
        user.setRole(ROLE_NORMAL);
        user.setState("1");
        user.setId(idWorker.nextId() + "");
        //保存到数据库中
        userDao.save(user);
        //返回结果
        return ResponseResult.GET(ResponseState.JOIN_IN_SUCCESS);
    }

    @Override
    public ResponseResult doLogin(String captcha, String captcha_key,
                                  User user, HttpServletRequest request, HttpServletResponse response) {
        String captchaValue = (String) redisUtil.get(Constants.user.KEY_CAPTCHA_CONTENT + captcha_key);
        if (!captcha.equals(captchaValue)) {
            return ResponseResult.FAILED("人类验证码不正确");
        }

        //有可能是邮箱，也有可能是用户名
        String userName = user.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.FAILED("账户不可为空");
        }

        String password = user.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可为空");
        }

        User userFromDb = userDao.findOneByUserName(userName);
        if (userFromDb == null) {
            userFromDb = userDao.findOneByEmail(userName);
        }
        if (userFromDb == null) {
            return ResponseResult.FAILED("用户名或密码不正确1");
        }
        //用户存在
        //对比密码
        log.info("password ==>" + password);
        log.info("passwordEncoding ==>" + userFromDb.getPassword());
        boolean matches = bCryptPasswordEncoder.matches(password, userFromDb.getPassword());
        if (!matches) {
            return ResponseResult.FAILED("用户名或密码不正确2");
        }
        //密码正确
        //判断用户状态，如果是非正常，则返回结果
        if (!"1".equals(userFromDb.getState())) {
            return ResponseResult.FAILED("当前账号已经被禁止");
        }
        createToken(response, userFromDb);
        return ResponseResult.SUCCESS("登录成功");
    }

    /**
     * 返回
     *
     * @param response
     * @param userFromDb
     * @return tokenkey
     */
    private String createToken(HttpServletResponse response, User userFromDb) {
        refreshTokenDao.deleteAllByUserId(userFromDb.getId());
        //生成token
        Map<String, Object> claims = ClaimsUtils.sobUser2Claims(userFromDb);
        String token = JwtUtil.createToken(claims);
        //返回token的md5值,token会保存在redis里
        //如果前端访问的时候，携带token的Md5key，从redis中获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis里，有效期为2个小时,key是tokenKey
        redisUtil.set(Constants.user.KEY_TOKEN + tokenKey, token, Constants.timeValue.HOUR_2);
        //把tokenkey写到cookies里
        Cookie cookie = new Cookie(COOKIE_TOKEN_KEY, tokenKey);
        //这个要动态获取，可以从request里获取
        CookieUtils.setUpCookie(response, COOKIE_TOKEN_KEY, tokenKey);
        //生成refreshToken
        String refreshTokenValue = JwtUtil.createRefreshToken(userFromDb.getId(), Constants.timeValue.MONTH);
        //保存到数据库里
        //RefreshToken,tokenKey,用户id,创建时间，更新时间
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(userFromDb.getId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenDao.save(refreshToken);
        return tokenKey;
    }

    /**
     * 本质就是检查用户是否有登录，如果登录了，就返回用户信息
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public User checkUser(HttpServletRequest request, HttpServletResponse response) {
        //拿到tokenKey
        String tokenKey = CookieUtils.getCookie(request, COOKIE_TOKEN_KEY);
        User user = parseByTokenKey(tokenKey);
        log.info("checkuser ---> "+user.getUserName());
        if (user == null) {
            //根据refreshToken去判断是否已经登录过了
            //1.去mysql数据库查询refreshToken
            RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
            //2.如果不存在，就是当前访问没有登录
            if (refreshToken == null) {
                return null;
            }
            //3.如果存在，就解析refreshToken
            try {
                Claims claims = JwtUtil.parseJWT(refreshToken.getRefreshToken());
                //5.如果refreshToken有效，创建新的token，和新的refreshToken
                String userId = refreshToken.getUserId();
                User userFromDb = userDao.findOneById(userId);
                //删掉refreshToken的记录
                refreshTokenDao.deleteById(refreshToken.getId());
                String newTokenKey = createToken(response, userFromDb);
                log.info("checkuser2 ---> "+parseByTokenKey(newTokenKey).getUserName());
                return parseByTokenKey(newTokenKey);
            } catch (Exception e1) {
                //4.如果refreshToken过期了，就当前访问没有登录，提示用户登录
                return null;
            }
        }
        return user;
        //说明有token,解析token

    }

    @Override
    public ResponseResult getUserInfo(String userId) {
        //从数据库里获取
        User user = userDao.findOneById(userId);
        //判断结果
        if (user == null) {
            //如果不存在，返回不存在
            return ResponseResult.FAILED("用户不存在");
        }
        //如果存在，就复制对象，清空密码、EMAIL、登录IP，注册IP
        String userJson = gson.toJson(user);
        User newUser = gson.fromJson(userJson, User.class);
        newUser.setPassword("");
        newUser.setEmail("");
        newUser.setReg_ip("");
        newUser.setLogin_ip("");

        //返回结果
        ResponseResult success = ResponseResult.SUCCESS("获取成功");
        success.setData(newUser);
        return success;
    }

    @Override
    public ResponseResult checkEmail(String email) {
        User user = userDao.findOneByEmail(email);
        return user==null? ResponseResult.FAILED("该邮箱未注册"):ResponseResult.SUCCESS("该邮箱已经注册");
    }

    @Override
    public ResponseResult checkUserName(String userName) {
        User user = userDao.findOneByUserName(userName);
        return user==null?ResponseResult.FAILED("该用户名未注册"):ResponseResult.SUCCESS("该用户名已经注册");
    }

    /**
     * 更新用户信息
     *
     * @param request
     * @param response
     * @param userId
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(HttpServletRequest request, HttpServletResponse response, String userId, User user) {
        //从token中解析出来的user,为了校验权限
        //只有用户自己才可以修改自己的信息
        User userFromKey = checkUser(request, response);
        if (userFromKey == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        User userAccount = userDao.findOneById(userFromKey.getId());
        //判断用户的id是否一致，如果一致才可以修改
        if(!userAccount.getId().equals(userId)){
            return ResponseResult.PERMISSION_FORBID();
        }
        //可以进行修改
        //可经修改的项
        //用户名
        String userName = user.getUserName();
        if (!TextUtils.isEmpty(user.getUserName())) {
            User userByUserName = userDao.findOneByUserName(userName);
            if (userByUserName != null) {
                return ResponseResult.FAILED("该用户名已注册");
            }
            userAccount.setUserName(userName);
        }
        //头像
        if (!TextUtils.isEmpty(user.getAvatar())) {
            userAccount.setAvatar(user.getAvatar());
        }
        //签名,可以为空
        userAccount.setSign(user.getSign());
        userDao.save(userAccount);
        //删除redis里的token，下一次请求，需要解析token的，就会根据refreshtoken重新创建一个。
        String cookie = CookieUtils.getCookie(request, COOKIE_TOKEN_KEY);
        redisUtil.del(cookie);
        return ResponseResult.SUCCESS("用户信息更新成功");


    }

    /**
     * 删除用户，并不是真正删除而是修改状态
     * 需要管理员权限
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult deleteUserById(String userId, HttpServletRequest request, HttpServletResponse response) {
//        //检验当前用户是谁
//        User currentUser = checkUser(request, response);
//        if (currentUser == null) {
//            return ResponseResult.ACCOUNT_NOT_LOGIN();
//        }
//        //判断角色
//        if (!ROLE_ADMIN.equals(currentUser.getRole())){
//            return ResponseResult.PERMISSION_FORBID();
//        }

        int result = userDao.deleteUserByState(userId);
        if (result>0){
            return ResponseResult.SUCCESS("删除成功");
        }else {
            return ResponseResult.FAILED("用户不存在");
        }
    }

    /**
     * 需要管理员权限
     * @param page
     * @param size
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult listUsers(int page, int size, HttpServletRequest request, HttpServletResponse response) {

        //可以获取用户列表
        //分页查询
        if (page<Constants.Page.DEFAULT_PAGE){
            page = 1;
        }
        //size也限制一下，每一页不得少于五个
        if (size <Constants.Page.MIN_SIZE){
            size = Constants.Page.MIN_SIZE;
        }

        //根据注册日期来排序
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(page - 1, size,sort);
        Page<User> all = userDao.listAllUserNoPassword(pageable);

        ResponseResult success = ResponseResult.SUCCESS("获取用户列表成功");
        success.setData(all);
        return success;
    }

    /**
     * 更新密码
     * @param verifyCode
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUserPassword(String verifyCode, User user) {
        //检查邮箱是否有填写
        String email = user.getEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱不可以为空");
        }
        //根据邮箱去redis里拿验证
        //进行对比
        String redisVerifyCode = (String) redisUtil.get(KEY_EMAIL_CODE_CONTENT + email);
        if (redisVerifyCode == null||!redisVerifyCode.equals(verifyCode)) {
            return ResponseResult.FAILED("验证码错误");
        }

        redisUtil.del(KEY_EMAIL_CODE_CONTENT + email);
        int result = userDao.updatePasswordByEmail(bCryptPasswordEncoder.encode(user.getPassword()), email);
        //修改密码
        return result > 0 ? ResponseResult.SUCCESS("密码修改成功") : ResponseResult.FAILED("修改密码失败");
    }

    private User parseByTokenKey(String tokenKey) {
        String token = (String) redisUtil.get(KEY_TOKEN+tokenKey);
        if (token != null) {
            try {
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.claims2User(claims);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

}
