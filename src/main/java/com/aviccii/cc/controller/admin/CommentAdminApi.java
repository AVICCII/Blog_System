package com.aviccii.cc.controller.admin;

import com.aviccii.cc.pojo.Comment;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/comment")
public class CommentAdminApi {

    @Autowired
    private ICommentService commentService;

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId")String commentId){
        return commentService.deleteCommentById(commentId);
    }


    @GetMapping("/{commentId}")
    public ResponseResult getComment(@PathVariable("commentId")String commentId){
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listImages(@RequestParam("page")int page,@RequestParam("size")int size){
        return commentService.listComments(page,size);
    }

    @PutMapping("/top/{commentId}")
    public ResponseResult TOPComment(@PathVariable("commentId")String commentId){
        return commentService.topComment(commentId);
    }
}
