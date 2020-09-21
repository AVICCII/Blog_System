package com.aviccii.cc.services;

import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
public interface IUserService {
    ResponseResult initManagerAccount(User user, HttpServletRequest request);
}
