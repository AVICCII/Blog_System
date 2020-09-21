package com.aviccii.cc.controller.portal;

import com.aviccii.cc.pojo.Comment;
import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/portal/comment")
public class CommentPortalApi {
    @PostMapping
    public ResponseResult postComment(@RequestBody Comment comment){
        return null;
    }

    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId")String commentId){
        return null;
    }

    @GetMapping("/list/{commentId}")
    public ResponseResult listComments(@PathVariable("commentId")String commentId){
        return null;
    }
}
