package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.ArticleDao;
import com.aviccii.cc.pojo.Article;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IArticleService;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.aviccii.cc.utils.Constants.Article.TITLE_MAX_LENGTH;

/**
 * 后期可以做一些定时发布的功能
 * 如果是多人博客系统，得考虑审核的问题---》成功，通知，审核不通过，也可通知
 *
 * 保存成草稿
 * 1.用户手动提交：发生页面跳转---》提交完成即可
 * 2.代码自动提交：每隔一段时间就会提交--》不会发生页面跳转--》多次提交--》如果没有唯一标识，会重新添加到数据库里
 *
 * 不管是哪种草稿，都必须有标题
 *
 * 每次用户发新文章之前--》先向后台请求一个唯一文章ID
 * 如果是更新文件，则不需要请求这个唯一ID
 *
 * 方案二：可以直接提交，后台判断有没有ID，如果没有ID，就新创建，并且ID作为此次返回的结果
 * 如果有ID，就修改已经存在的内容。
 *
 * 推荐做法：自动保存草稿，在前端本地完成，也就是保存在本地
 * 如果是用户
 *
 *
 * 防止重复提交
 * 可以通过ID的方式
 * 通过TOKEN_KEY的提交频率来计算，如果30秒内有多次提交，只有最前的一次有效
 * 其他的提交，直接return,提示用户不要太频繁操作
 *
 * 前端的处理：点击了提交之后，禁止按钮可以使用，等到有响应的结果后，再改变按钮的状态
 *
 * @author aviccii 2020/10/21
 * @Discrimination
 */
@Service
@Transactional
public class ArticleServiceImpl extends BaseSerive implements IArticleService {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ArticleDao articleDao;

    @Override
    public ResponseResult postArticle(Article article) {
        //检查用户，获取到用户对象
        User user = iUserService.checkUser();
        //未登录
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检查数据
        //title,分类id，内容，类型，摘要，标签
        String title = article.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("标题不可以为空");
        }
        if (title.length()>TITLE_MAX_LENGTH) {
            return ResponseResult.FAILED("文章标题不可以超过"+TITLE_MAX_LENGTH+"个字符");
        }
        String content = article.getContent();
        if (TextUtils.isEmpty(content)) {
            return ResponseResult.FAILED("内容不可以为空");
        }
        String type = article.getType();
        if (TextUtils.isEmpty(type)) {
            return ResponseResult.FAILED("类型不可以为空");
        }
        if (!"0".equals(type)&&"1".equals(type)) {
            return ResponseResult.FAILED("类型格式错误");
        }
        String summary = article.getSummary();
        if (TextUtils.isEmpty(summary)) {
            return ResponseResult.FAILED("摘要不可以为空");
        }
        if (summary.length()> Constants.Article.SUMMARY_MAX_LENGTH) {
            return ResponseResult.FAILED("摘要不可以超过"+Constants.Article.SUMMARY_MAX_LENGTH+"字符");
        }
        String labels = article.getLabels();
        //标签-标签1-标签2
        if (TextUtils.isEmpty(labels)) {
            return ResponseResult.FAILED("标签不可以为空");
        }

        //补充数据，ID,创建时间，用户id,更新时间
        article.setId(idWorker.nextId()+"");
        article.setUserId(user.getId());
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        //保存到数据库里
        articleDao.save(article);
        //TODO：保存到搜索的数据库里
        //返回结果
        return ResponseResult.SUCCESS("文章发表成功");
    }
}