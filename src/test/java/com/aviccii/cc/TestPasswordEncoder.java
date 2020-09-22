package com.aviccii.cc;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author aviccii 2020/9/22
 * @Discrimination
 */
public class TestPasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
        //$2a$10$EbbjCgiy5bXPYbinVo9rDOI1hUY7f9jSo5rtMEqOzcrDvnP849ENm
        System.out.println("encode == > "+encode);
        //验证登录流程
        //1.提交密码
        //2.跟数据库中的密文匹配如何判断是否准确
        String originalPassword = "123456";
        boolean matches = passwordEncoder.matches(originalPassword, "$2a$10$EbbjCgiy5bXPYbinVo9rDOI1hUY7f9jSo5rtMEqOzcrDvnP849ENm");
        System.out.println("密码是否正确 == >"+ matches);
    }
}
