package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Article;
import com.aviccii.cc.pojo.ArticleNoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author aviccii 2020/10/21
 * @Discrimination
 */
public interface ArticleNoContentDao extends JpaRepository<ArticleNoContent,String>, JpaSpecificationExecutor<ArticleNoContent> {
    Article findOneById(String id);
}
