package com.aviccii.cc.controller;

import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aviccii 2020/10/14
 * @Discrimination
 */
@RestController
public class ErrorPageController {

    @GetMapping("/404")
    public ResponseResult page404(){
        return ResponseResult.ERROR_404();
    }

    @GetMapping("/403")
    public ResponseResult page403(){
        return ResponseResult.ERROR_403();
    }

    @GetMapping("/504")
    public ResponseResult page504(){
        return ResponseResult.ERROR_504();
    }

    @GetMapping("/505")
    public ResponseResult page505(){
        return ResponseResult.ERROR_505();
    }

}
