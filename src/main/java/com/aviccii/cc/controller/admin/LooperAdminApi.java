package com.aviccii.cc.controller.admin;

import com.aviccii.cc.pojo.Looper;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ILoopService;
import com.aviccii.cc.services.impl.LoopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/loop")
public class LooperAdminApi {

    @Autowired
    private ILoopService loopService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addLoop(@RequestBody Looper looper){
        return loopService.addLoop(looper);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{loopId}")
    public ResponseResult deleteLoop(@PathVariable("loopId")String loopId){
        return loopService.deleteLoop(loopId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/{loopId}")
    public ResponseResult uploadLoop(@PathVariable("loopId")String loopId,@RequestBody Looper looper){
        return loopService.updateLoop(loopId,looper);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/{loopId}")
    public ResponseResult getLoop(@PathVariable("loopId")String loopId){
        return loopService.getLoop(loopId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listLoops(@PathVariable("page")int page,@PathVariable("size")int size){
        return loopService.listLooopers(page,size);
    }
}
