package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Article;
import com.aviccii.cc.pojo.ArticleNoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author aviccii 2020/10/21
 * @Discrimination
 */
public interface ArticleNoContentDao extends JpaRepository<ArticleNoContent,String>, JpaSpecificationExecutor<ArticleNoContent> {
    Article findOneById(String id);

    @Query(nativeQuery = true,value = "select * from `tb_article` where `labels` like ?1 and `id` !=?3 and (`state` = '1' or `state` = '3') LIMIT ?2")
    List<ArticleNoContent> listArticleByLikeLabel(String label,int size,String originalArticleId);

    @Query(nativeQuery = true,value = "select * from `tb_article` where `id` !=? and (`state` = '1' or `state` = '3') order by `create_time` DESC limit ?")
    List<ArticleNoContent> listLastedArticleBySize(String articleId, int size);
}
