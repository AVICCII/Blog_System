package com.aviccii.cc.services;

import com.aviccii.cc.pojo.Comment;
import com.aviccii.cc.response.ResponseResult;

/**
 * @author aviccii 2020/10/26
 * @Discrimination
 */
public interface ICommentService {
    ResponseResult postComment(Comment comment);

    ResponseResult listCommentByArticleId(String articleId, int page, int size);

    ResponseResult deleteCommentById(String commentId);
}
