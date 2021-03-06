package com.aviccii.cc.controller.portal;

import com.aviccii.cc.pojo.Comment;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/portal/comment")
public class CommentPortalApi {


    @Autowired
    private ICommentService commentService;

    @PostMapping
    public ResponseResult postComment(@RequestBody Comment comment){
        return commentService.postComment(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId")String commentId){
        return commentService.deleteCommentById(commentId);
    }

    @GetMapping("/list/{articleId}/{page}/{size}")
    public ResponseResult listComments(@PathVariable("articleId")String articleId,@PathVariable("page")int page,
                                       @PathVariable("size")int size){
        return commentService.listCommentByArticleId(articleId,page,size);
    }
}
