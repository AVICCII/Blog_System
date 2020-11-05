package com.aviccii.cc.interceptor;

import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.CookieUtils;
import com.aviccii.cc.utils.RedisUtil;
import com.aviccii.cc.utils.TextUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author aviccii 2020/11/4
 * @Discrimination
 */
@Component
@Slf4j
public class ApiInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Gson gson;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            String name = method.getName();
            log.info("method name == >" + name);
            CheckTooFrequentCommit methodAnnotation = handlerMethod.getMethodAnnotation(CheckTooFrequentCommit.class);
            if (methodAnnotation != null) {
                String methodName = handlerMethod.getMethod().getName();
                //所有提交的方法，必须用户登录的，所以使用token作为key来记录请求频率
                String tokenKey = CookieUtils.getCookie(request, Constants.user.COOKIE_TOKEN_KEY);
                log.info("tokenKey -||- >" + tokenKey);
                if (!TextUtils.isEmpty(tokenKey)) {
                    String hasCommit = (String) redisUtil.get(Constants.user.KEY_COMMIT_TOKEN_RECORD + tokenKey);
                    if (!TextUtils.isEmpty(hasCommit)) {
                        //从redis里获取，判断是否存在，如果存在，则返回提交太频繁
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        ResponseResult failed = ResponseResult.FAILED("提交过于频繁请稍后");
                        PrintWriter writer = response.getWriter();
                        writer.write(gson.toJson(failed));
                        writer.flush();
                        return false;
                    } else {
                        //如果不存在，说明可以提交，并且记录这次提交，有效期为30秒
                        redisUtil.set(Constants.user.KEY_COMMIT_TOKEN_RECORD + tokenKey, "true", Constants.timeValue.MIN_2);
                    }
                }
                //去判断是否真的提交过于频繁
                log.info("check commit too frequent...");
            }
        }
        return true;
    }
}
