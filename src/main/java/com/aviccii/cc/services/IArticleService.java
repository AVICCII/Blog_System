package com.aviccii.cc.services;

import com.aviccii.cc.pojo.Article;
import com.aviccii.cc.response.ResponseResult;

/**
 * @author aviccii 2020/10/21
 * @Discrimination
 */
public interface IArticleService {

    ResponseResult postArticle(Article article);
}
