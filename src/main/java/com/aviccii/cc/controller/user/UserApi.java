package com.aviccii.cc.controller.user;

import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.response.ResponseState;
import com.aviccii.cc.services.IUserService;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * 修改密码
     *
     * @param user
     * @return
     * @ PostMapping 和 @PutMapping 作用等同，都是用来向服务器提交信息。
     * 如果是添加信息，倾向于用@PostMapping，如果是更新信息，倾向于用@PutMapping。两者差别不是很明显。
     */
    @PutMapping("/password/{verifyCode}")
    public ResponseResult updatePassword(@PathVariable("verifyCode") String verifyCode, @RequestBody User user) {
        return iUserService.updateUserPassword(verifyCode,user);
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

    /**
     * 允许用户修改的内容
     * 1.头像
     * 2.用户名 (唯一的)
     * 3.密码 (单独修改)
     * 4.签名
     * 5.用户的email(唯一的，单独修改)
     * @param userId
     * @param user
     * @return
     */

    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo(HttpServletResponse response, HttpServletRequest request,
                                         @PathVariable("userId") String userId, @RequestBody User user) {
        return iUserService.updateUserInfo(request,response,userId,user);
    }

    /**
     * 获取用户列表
     * 权限：管理员权限
     *
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listUsers(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                     HttpServletRequest request,HttpServletResponse response) {
        return iUserService.listUsers(page,size,request,response);
    }

    /**
     * 需要管理员权限
     * @param userId
     * @return
     */
//    @PreAuthorize("hasRole('role_admin')")
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(HttpServletResponse response, HttpServletRequest request,
                                     @PathVariable("userId") String userId) {
        //判断当前的操作用户
        //根据用户角色判断是否可以操作
        //TODO：通过注解方式来控制权限
        return iUserService.deleteUserById(userId,request,response);
    }

    /**
     * 检查该email是否已经注册
     * @param email 邮箱地址
     * @return  SUCCESS --> 已经注册，FAIL --> 没有注册
     */
    @ApiResponses({
            @ApiResponse(code = 20000,message = "表示当前邮箱已经注册"),
            @ApiResponse(code = 40000,message = "表示当前邮箱未注册")
    })
    @GetMapping("/email")
    public ResponseResult checkEmail(@RequestParam("email")String email){
        return iUserService.checkEmail(email);
    }


    /**
     * 检查该email是否已经注册
     * @param userName 邮箱地址
     * @return  SUCCESS --> 已经注册，FAIL --> 没有注册
     */
    @ApiResponses({
            @ApiResponse(code = 20000,message = "表示当前用户名已经注册"),
            @ApiResponse(code = 40000,message = "表示当前用户名未注册")
    })
    @GetMapping("/user_name")
    public ResponseResult checkUserName(@RequestParam("userName")String userName){
        return iUserService.checkUserName(userName);
    }
}
