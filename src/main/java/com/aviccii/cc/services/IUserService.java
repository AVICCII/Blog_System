package com.aviccii.cc.services;

import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
public interface IUserService {
    ResponseResult initManagerAccount(User user, HttpServletRequest request);

    void createCaptcha(HttpServletResponse response,String captchaKey) throws IOException;

    ResponseResult sendemail(String type,HttpServletRequest request,String emailAddress);

    ResponseResult register(User user, String emailCode, String captchaCode, String captchaKey, HttpServletRequest request);

    ResponseResult doLogin(String captcha, String captcha_key, User user, HttpServletRequest request, HttpServletResponse response);

    User checkUser();

    ResponseResult getUserInfo(String userId);

    ResponseResult checkEmail(String email);

    ResponseResult checkUserName(String userName);

    ResponseResult updateUserInfo( String userId, User user);

    ResponseResult deleteUserById(String userId);

    ResponseResult listUsers(int page, int size, String userName, String email);

    ResponseResult updateUserPassword(String verifyCode, User user);

    ResponseResult updateEmail(String email, String verifyCode);

    ResponseResult doLogout();

    ResponseResult parseToken();

    ResponseResult resetPassword(String userId, String password);

}
