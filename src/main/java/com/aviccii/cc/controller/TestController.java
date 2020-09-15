package com.aviccii.cc.controller;

import com.aviccii.cc.pojo.Student;
import com.aviccii.cc.response.ResponseResult;
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
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String helloWorld(){
        return "hello world!";
    }

    @GetMapping("/student")
    public ResponseResult getStudent(){
        Student student = new Student("张三", "男", 27);
        ResponseResult responseResult =  ResponseResult.SUCCESS();
        responseResult.setData(student);
        return responseResult;
    }

}
