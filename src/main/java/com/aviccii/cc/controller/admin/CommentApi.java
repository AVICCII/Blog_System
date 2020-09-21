package com.aviccii.cc.controller.admin;

import com.aviccii.cc.pojo.Comment;
import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/comment")
public class CommentApi {

    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId")String commentId){
        return null;
    }


    @GetMapping("/{commentId}")
    public ResponseResult getComment(@PathVariable("commentId")String commentId){
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listImages(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }

    @PutMapping("/top/{commentId}")
    public ResponseResult TOPComment(@PathVariable("commentId")String commentId){
        return null;
    }
}
