package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.ArticleDao;
import com.aviccii.cc.dao.ArticleNoContentDao;
import com.aviccii.cc.pojo.Article;
import com.aviccii.cc.pojo.ArticleNoContent;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IArticleService;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.aviccii.cc.utils.Constants.Article.*;

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

    @Autowired
    private ArticleNoContentDao articleNoContentDao;

    @Override
    public ResponseResult postArticle(Article article) {
        //两种，草稿和发布
        String state = article.getState();
        if (!Constants.Article.STATE_PUBLISH.equals(state)&&!Constants.Article.STATE_DRAFT.equals(state)){
            return ResponseResult.FAILED("不支持此操作");
        }

        //检查用户，获取到用户对象
        User user = iUserService.checkUser();
        //未登录
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检查数据
        //title,分类id，内容，类型，摘要，标签
        String title = article.getTitle();
        //以下检查是发布检查，草稿不需要检查
        if (STATE_PUBLISH.equals(state)) {
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
            if (!"0".equals(type)&&!"1".equals(type)) {
                return ResponseResult.FAILED("类型格式错误");
            }
            String summary = article.getSummary();
            if (TextUtils.isEmpty(summary)) {
                return ResponseResult.FAILED("摘要不可以为空");
            }
            if (summary.length()> Constants.Article.SUMMARY_MAX_LENGTH) {
                return ResponseResult.FAILED("摘要不可以超过"+Constants.Article.SUMMARY_MAX_LENGTH+"字符");
            }
            String labels = article.getLabel();
            //标签-标签1-标签2
            if (TextUtils.isEmpty(labels)) {
                return ResponseResult.FAILED("标签不可以为空");
            }
        }else{

        }

        String articleId = article.getId();
        if (TextUtils.isEmpty(articleId)) {
            //新内容，数据里没有的
            //补充数据，ID,创建时间，用户id,更新时间
            article.setId(idWorker.nextId()+"");
            article.setCreateTime(new Date());

        }else {
            //更新内容,对状态进行处理，如果是已经发布的，则不能再作为草稿
            Article articleFromDb = articleDao.findOneById(articleId);
            if (articleFromDb.getState().equals(STATE_PUBLISH)&&STATE_DRAFT.equals(state)) {
                //已经发布了，只能更新，不能保存为草稿
                return ResponseResult.FAILED("已经发布的文章不支持保存成草稿");
            }

        }

        article.setUserId(user.getId());
        article.setUpdateTime(new Date());
        //保存到数据库里
        articleDao.save(article);

        //TODO：保存到搜索的数据库里
        //返回结果,只有一种case使用到这个id
        //如果要做程序自动保存成草稿，比如说每30秒保存一次，就需要加上这个id，否则会创建多个item
        ResponseResult success = ResponseResult.SUCCESS(STATE_DRAFT.equals(state)?"草稿保存成功":"文章发表成功");
        success.setData(article.getId());
        return success;
    }

    /**
     * 管理中心，获取文章列表
     * @param page  页码
     * @param size  每一页的数量
     * @param state     状态：已经删除、草稿、已经发布的
     * @param keyword   标题关键字(搜索关键字)
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult listArticle(int page, int size, String state, String keyword, String categoryId) {
        //处理一下size和page
        page = checkPage(page);
        size = checkSize(size);
        //创建分页和排序条件
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        //开始查询
        Page<ArticleNoContent> all = articleNoContentDao.findAll(new Specification<ArticleNoContent>() {
            @Override
            public Predicate toPredicate(Root<ArticleNoContent> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                //判断是否有传参数
                if (!TextUtils.isEmpty(state)) {
                    Predicate statePre = criteriaBuilder.equal(root.get("state").as(String.class), state);
                    predicates.add(statePre);
                }
                if (!TextUtils.isEmpty(categoryId)) {
                    Predicate categoryIdPre = criteriaBuilder.equal(root.get("categoryId").as(String.class), categoryId);
                    predicates.add(categoryIdPre);
                }
                if (!TextUtils.isEmpty(keyword)) {
                    Predicate titlePre = criteriaBuilder.like(root.get("title").as(String.class), "%" + keyword + "%");
                    predicates.add(titlePre);
                }
                Predicate[] preArray = new Predicate[predicates.size()];
                predicates.toArray(preArray);
                return criteriaBuilder.and(preArray);
            }
        }, pageable);
        //处理查询条件
        //返回结果
        ResponseResult success = ResponseResult.SUCCESS("获取列表成功");
        success.setData(all);
        return success;
    }

    /**
     * 如果有审核机制，审核中的文章-->只有管理员和作者自己能获取
     * 有草稿、删除、置顶的、已经发布的
     * 删除的不能获取，其他的都能获取
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult getArticleById(String articleId) {
        //查询出文章
        Article article = articleDao.findOneById(articleId);
        if (article == null) {
            return ResponseResult.FAILED("文章不存在");
        }
        //判断文章状态
        String state = article.getState();
        if (STATE_PUBLISH.equals(state)||STATE_TOP.equals(state)) {
             //可以返回
            ResponseResult success = ResponseResult.SUCCESS("获取文章成功");
            success.setData(article);
            return success;
        }
        //如果是删除/草稿，需要管理员角色
        User user = iUserService.checkUser();
        String role = user.getRole();
        if (!Constants.user.ROLE_ADMIN.equals(role)) {
            return ResponseResult.PERMISSION_FORBID();
        }
        //返回结果
        ResponseResult success = ResponseResult.SUCCESS("获取文章成功");
        success.setData(article);
        return success;
    }

    /**
     * 更新文章内容
     *
     * 该接口只支持修改内容：标题、内容、标签、分类、摘要
     *
     * @param articleId 文章id
     * @param article 文章
     * @return
     */
    @Override
    public ResponseResult updateArticle(String articleId, Article article) {
        //先找出来
        Article articleFromDb = articleDao.findOneById(articleId);
        if (articleFromDb == null) {
            return ResponseResult.FAILED("文章不存在");
        }
        //内容修改
        String title = article.getTitle();
        if (!TextUtils.isEmpty(title)) {
            articleFromDb.setTitle(title);
        }
        String summary = article.getSummary();
        if (!TextUtils.isEmpty(summary)) {
            articleFromDb.setSummary(summary);
        }
        String content = article.getContent();
        if (!TextUtils.isEmpty(content)) {
            articleFromDb.setContent(content);
        }
        String label = article.getLabel();
        if (!TextUtils.isEmpty(label)) {
            articleFromDb.setLabel(label);
        }

        String categoryId = article.getCategoryId();
        if (!TextUtils.isEmpty(categoryId)) {
            articleFromDb.setCategoryId(categoryId);
        }
        articleFromDb.setCover(article.getCover());
        articleFromDb.setUpdateTime(new Date());
        articleDao.save(articleFromDb);
        //返回结果
            return ResponseResult.SUCCESS("文章更新成功");
    }

    /**
     * 删除文章
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult deleteArticleById(String articleId) {
        int result = articleDao.deleteAllById(articleId);
        if (result>0){
            return ResponseResult.SUCCESS("文章删除成功");
        }
        return ResponseResult.FAILED("文章不存在");
    }
}
