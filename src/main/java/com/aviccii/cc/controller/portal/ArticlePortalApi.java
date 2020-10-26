package com.aviccii.cc.controller.portal;

import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IArticleService;
import com.aviccii.cc.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */

@RestController
@RequestMapping("/portal/article")
public class ArticlePortalApi {

    @Autowired
    private IArticleService articleService;


    /**
     * 获取文章列表
     *  权限，所有用户
     *  状态：必须已经发布，置顶的由另外一个接口获取，其他的不可以从此接口获取
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticle(@PathVariable("page")int page,@PathVariable("size")int size){
        return articleService.listArticle(page,size,Constants.Article.STATE_PUBLISH,null,null );
    }

    @GetMapping("/list/{categoryId}/{page}/{size}")
    public ResponseResult listArticleByCategory(@PathVariable("categoryId")String categoryId,
                                      @PathVariable("page")int page,
                                      @PathVariable("size")int size){
        return articleService.listArticle(page,size,Constants.Article.STATE_PUBLISH,null,categoryId);
    }

    /**
     * 获取文章详情
     * 权限：任意用户
     *
     * 内容过滤：只允许拿置顶的或者已经发表的
     *
     * @param articleId
     * @return
     */
    @GetMapping("/{articleId}")
    public ResponseResult getArticleDetail(@PathVariable("articleId")String articleId){
        return articleService.getArticleById(articleId);
    }

    /**
     * 通过标签来计算匹配度
     * 标签：有一个，多个（五个以内，包括五个）
     * 从里面随机拿一个标签出来--->每次获取的推荐文章，不那么雷同
     * 通过标签查询类似的文章，所包含此标签的文章
     * 如果没有相关文章，则从数据中获取最新的文章
     * @param articleId
     * @return
     */
    @GetMapping("/recommend/{articleId}/{size}")
    public ResponseResult getRecommendArticles(@PathVariable("articleId")String articleId,
                                               @PathVariable("size")int size){
        return articleService.listRecommendArticle(articleId,size);
    }

    @GetMapping("/top")
    public ResponseResult getTopArticle(){
        return articleService.listTopArticles();
    }

    @GetMapping("/list/label/{label}/{page}/{size}")
    public ResponseResult listArticleByLabel(@PathVariable("label")String label,
                                                @PathVariable("page")int page,
                                                @PathVariable("size")int size){
        return articleService.listArticleByLabel(page,size,label);
    }


    /**
     * 获取标签云，用户点击标签，就会通过标签获取到相关的文章列表
     * @param size
     * @return
     */
    @GetMapping("/label/{size}")
    public ResponseResult getLabels(@PathVariable("size")int size){
        return articleService.listLabels(size);
    }
}
