package com.aviccii.cc.controller;

import com.aviccii.cc.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/15
 * @Discrimination
 */
//@Controller
//@ResponseBody  13+14代码等效16

@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {

    public static final Logger log  = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String helloWorld(){
        return "hello world!";
    }



}
