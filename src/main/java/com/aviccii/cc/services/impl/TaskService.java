package com.aviccii.cc.services.impl;

import com.aviccii.cc.utils.EmailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author aviccii 2020/9/25
 * @Discrimination
 */
@Service
public class TaskService {

    @Async
    public void sendEmailVerifyCode(String VerifyCode,String EmailAddress) throws Exception {
        EmailSender.sendRegisterVerifyCode(String.valueOf(VerifyCode),EmailAddress);

    }
}
