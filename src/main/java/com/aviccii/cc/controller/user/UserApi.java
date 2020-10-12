package com.aviccii.cc.controller.user;

import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IUserService;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * @author aviccii 2020/9/17
 * @Discrimination
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody User user, HttpServletRequest request) {
        log.info("user name ==========>" + user.getUserName());
        log.info("user password ==========>" + user.getPassword());
        log.info("user email ==========>" + user.getEmail());
        return iUserService.initManagerAccount(user, request);
    }

    @PostMapping
    public ResponseResult register(@RequestBody User user, @RequestParam("email_code") String emailCode,
                                   @RequestParam("captcha_code") String captchaCode,
                                    @RequestParam("captcha_key")String captchaKey,
                                   HttpServletRequest request) {
        return iUserService.register(user, emailCode,captchaCode,captchaKey,request);

    }

    /**
     * 登录sign-up
     * 需要提交的数据
     * 1.用户账号- 可以昵称，可以邮箱--->做了唯一处理
     * 2.密码
     * 3.图灵验证码
     * 4.图灵验证的key
     * @param captcha_key 图灵验证码
     * @param captcha   图灵验证码的key
     * @param user  用户bean类，封装着账号和密码
     * @return
     */
    @PostMapping("/{captcha}/{captcha_key}")
    public ResponseResult login(@PathVariable("captcha_key")String captcha_key,
                                @PathVariable("captcha") String captcha,
                                @RequestBody User user,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        return iUserService.doLogin(captcha,captcha_key,user,request,response);
    }

    public static final int[] captcha_font_types = {Captcha.FONT_1, Captcha.FONT_2,
            Captcha.FONT_3, Captcha.FONT_4, Captcha.FONT_5, Captcha.FONT_6,
            Captcha.FONT_7, Captcha.FONT_8, Captcha.FONT_9, Captcha.FONT_10};


    /**
     * 图灵验证码
     * 有效时常10分钟
     *
     * @param response
     * @param captchaKey
     * @throws IOException
     * @throws FontFormatException
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, @RequestParam("captcha_key") String captchaKey) throws IOException, FontFormatException {
        iUserService.createCaptcha(response, captchaKey);


    }

    /**
     * 在处理方法入参处使用 @RequestParam 可以把请求参数传递给请求方法
     *
     * @param emailAddress
     * @return
     */
    @GetMapping("/verify_code")
    public ResponseResult sendVerifyCode(HttpServletRequest request,
                                         @RequestParam("type") String type, @RequestParam("email") String emailAddress) {
        log.info("email ==> " + emailAddress);
        return iUserService.sendemail(type, request, emailAddress);
    }

    /**
     * @param user
     * @return
     * @ PostMapping 和 @PutMapping 作用等同，都是用来向服务器提交信息。
     * 如果是添加信息，倾向于用@PostMapping，如果是更新信息，倾向于用@PutMapping。两者差别不是很明显。
     */
    @PutMapping("/password/{userId}")
    public ResponseResult updatePassword(@PathVariable("userId") String userId, @RequestBody User user) {
        return null;
    }

    /**
     * 使用@PathVariable可以快速的访问URL中的部分内容。
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId) {

        return iUserService.getUserInfo(userId);
    }

    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo(@PathVariable("userId") String userId, @RequestBody User user) {
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listUsers(@RequestParam("page") int page, @RequestParam("size") int size) {
        return null;
    }

    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId") String userId) {
        return null;
    }

}
