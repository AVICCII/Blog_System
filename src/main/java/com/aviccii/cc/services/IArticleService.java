package com.aviccii.cc.services;

import com.aviccii.cc.pojo.Article;
import com.aviccii.cc.response.ResponseResult;

/**
 * @author aviccii 2020/10/21
 * @Discrimination
 */
public interface IArticleService {

    ResponseResult postArticle(Article article);

    ResponseResult listArticle(int page, int size, String state, String keyword, String categoryId);

    ResponseResult getArticleById(String articleId);

    ResponseResult updateArticle(String articleId, Article article);

    ResponseResult deleteArticleById(String articleId);

    ResponseResult deleteArticleByState(String articleId);

    ResponseResult TopArticle(String articleId);

    ResponseResult listTopArticles();


    ResponseResult listRecommendArticle(String articleId, int size);
}
