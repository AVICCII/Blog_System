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

    /**
     * 如果是多用户，用户不可以删，删除只是修改状态
     * 管理员可以删除
     *
     * 这里做删除
     *
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{articleId}")
    public ResponseResult deleteArticle(@PathVariable("articleId")String articleId){
        return iArticleService.deleteArticleById(articleId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId")String articleId,@RequestBody Article article){
        return iArticleService.updateArticle(articleId,article);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId")String articleId){
        return iArticleService.getArticleById(articleId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticles(@PathVariable("page")int page,
                                       @PathVariable("size")int size,
                                       @RequestParam(value = "state",required = false)String state,
                                       @RequestParam(value = "keyword",required = false)String keyword,
                                       @RequestParam(value = "categoryId",required = false)String categoryId){
        return iArticleService.listArticle(page,size,state,keyword,categoryId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/state/{articleId}/{state}")
    public ResponseResult updateArticleState(@PathVariable("articleId")String articleId,@PathVariable("state")String state){
        return null;
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{articleId}")
    public ResponseResult updateArticleState(@PathVariable("articleId")String articleId){
        return null;
    }
}
