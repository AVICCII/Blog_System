package com.aviccii.cc.services;

import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import org.springframework.stereotype.Service;

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

    ResponseResult sendemail(HttpServletRequest request,String emailAddress);
}
