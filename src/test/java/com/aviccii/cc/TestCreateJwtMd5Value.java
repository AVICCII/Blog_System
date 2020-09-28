package com.aviccii.cc;

import org.springframework.util.DigestUtils;

/**
 * @author aviccii 2020/9/28
 * @Discrimination
 */
public class TestCreateJwtMd5Value {
    public static void main(String[] args) {
        String jwtKeyMd5Str = DigestUtils.md5DigestAsHex("blog_system_-=".getBytes());
        System.out.println(jwtKeyMd5Str);
    }
}
