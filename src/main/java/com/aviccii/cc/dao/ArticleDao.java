package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

/**
 * @author aviccii 2020/10/21
 * @Discrimination
 */
public interface ArticleDao extends JpaRepository<Article,String>, JpaSpecificationExecutor<Article> {
    Article findOneById(String id);

    @Modifying
    int deleteAllById(String articleId);
}
