package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author aviccii 2020/10/21
 * @Discrimination
 */
public interface ArticleDao extends JpaRepository<Article,String>, JpaSpecificationExecutor<Article> {
    Article findOneById(String id);

    @Modifying
    int deleteAllById(String articleId);

    @Modifying
    @Query(nativeQuery = true,value = "update `tb_article` set `state` = '0' where `id` = ?")
    int deleteArticleByState(String ArticleId);

    @Modifying
    @Query(nativeQuery = true,value = "update `tb_article` set `state` = '3' where `id` = ?")
    int topArticle(String ArticleId);

    @Query(nativeQuery = true,value = "select `labels` from `tb_article` where `id`=?")
    String listArticleLabelsById(String articleId);
}
