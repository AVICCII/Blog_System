package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author aviccii 2020/10/9
 * @Discrimination
 */
public interface CommentDao extends JpaRepository<Comment,String>, JpaSpecificationExecutor<Comment> {
    Comment findOneById(String commentId);
}
