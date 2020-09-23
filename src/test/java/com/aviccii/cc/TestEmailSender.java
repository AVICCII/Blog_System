package com.aviccii.cc;

import com.aviccii.cc.utils.EmailSender;

import javax.mail.MessagingException;

/**
 * @author aviccii 2020/9/23
 * @Discrimination
 */
public class TestEmailSender {

    public static void main(String[] args) throws MessagingException {
        EmailSender.subject("测试邮件发送")
                .from("阳光沙滩博客系统")
                .text("这是发送的内容：ab12rf")
                .to("863134471@qq.com")
                .send();
    }
}