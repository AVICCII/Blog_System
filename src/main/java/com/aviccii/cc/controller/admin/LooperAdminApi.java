package com.aviccii.cc.controller.admin;

import com.aviccii.cc.pojo.Looper;
import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/loop")
public class LooperAdminApi {
    @PostMapping
    public ResponseResult addLoop(@RequestBody Looper looper){
        return null;
    }

    @DeleteMapping("/{loopId}")
    public ResponseResult deleteLoop(@PathVariable("loopId")String loopId){
        return null;
    }

    @PutMapping("/{loopId}")
    public ResponseResult uploadLoop(@PathVariable("loopId")String loopId){
        return null;
    }

    @GetMapping("/{loopId}")
    public ResponseResult getLoop(@PathVariable("loopId")String loopId){
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listLoops(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }
}
