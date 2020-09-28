package com.aviccii.cc;

import com.aviccii.cc.utils.JwtUtil;

/**
 * @author aviccii 2020/9/28
 * @Discrimination
 */
public class TestCreateToken {
    public static void main(String[] args) {
        String token = JwtUtil.createJWT(
                //用户id
                "760078116340105216",
                //用户名
                "新用户注册名",
                null
        );
        System.out.println(token);
    }
}
