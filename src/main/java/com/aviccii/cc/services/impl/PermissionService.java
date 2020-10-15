package com.aviccii.cc.services.impl;

import com.aviccii.cc.pojo.User;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.CookieUtils;
import com.aviccii.cc.utils.TextUtils;
import com.sun.org.apache.bcel.internal.generic.FSUB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aviccii 2020/10/14
 * @Discrimination
 */
@Service("permission")
public class PermissionService {

    @Autowired
    private IUserService userService;

    /**
     * 判断是不是管理员
     * @return
     */
    public boolean admin() {
        //拿到request和response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        String tokenKey = CookieUtils.getCookie(request, Constants.user.COOKIE_TOKEN_KEY);
        //没有令牌的key,没有登录，不用往下执行了
        if (TextUtils.isEmpty(tokenKey)){
            return false;
        }

        User user = userService.checkUser();
        if (user == null) {
            return false;
        }
        if (Constants.user.ROLE_ADMIN.equals(user.getRole())) {
            //管理员
            return true;
        }
        return false;
    }
}
