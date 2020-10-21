package com.aviccii.cc.controller.admin;

import com.aviccii.cc.pojo.Article;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/article")
public class ArticleAdminApi {

    @Autowired
    private IArticleService iArticleService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult postArticle(@RequestBody Article article){
        return iArticleService.postArticle(article);
    }

    @DeleteMapping("/{articleId}")
    public ResponseResult deleteArticle(@PathVariable("articleId")String articleId){
        return null;
    }

    @PutMapping("/{articleId}")
    public ResponseResult uploadArticle(@PathVariable("articleId")String articleId){
        return null;
    }

    @GetMapping("/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId")String articleId){
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listArticles(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }

    @PutMapping("/state/{articleId}/{state}")
    public ResponseResult updateArticleState(@PathVariable("articleId")String articleId,@PathVariable("state")String state){
        return null;
    }

    @PutMapping("/top/{articleId}")
    public ResponseResult updateArticleState(@PathVariable("articleId")String articleId){
        return null;
    }
}
