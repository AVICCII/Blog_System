package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.ArticleNoContentDao;
import com.aviccii.cc.dao.CommentDao;
import com.aviccii.cc.pojo.Article;
import com.aviccii.cc.pojo.ArticleNoContent;
import com.aviccii.cc.pojo.Comment;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ICommentService;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.EmailSender;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * @author aviccii 2020/10/26
 * @Discrimination
 */
@Service
@Transactional
public class CommentServiceImpl extends BaseSerive implements ICommentService {

    @Autowired
    private IUserService userService;

    @Autowired
    private ArticleNoContentDao articleNoContentDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CommentDao commentDao;

    /**
     * 发表评论
     *
     * @param comment
     * @return
     */
    @Override
    public ResponseResult postComment(Comment comment) {
        //检查用户是否登录
        User user = userService.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检查内容
        String articleId = comment.getArticleId();
        if (TextUtils.isEmpty(articleId)) {
            return ResponseResult.FAILED("文章ID不可以为空");
        }
        ArticleNoContent article = articleNoContentDao.findOneById(articleId);
        if (article == null) {
            return ResponseResult.FAILED("文章不存在.");
        }
        String content = comment.getContent();
        if (TextUtils.isEmpty(content)) {
            return ResponseResult.FAILED("评论内容不可为空.");
        }
        //补全内容
        comment.setId(idWorker.nextId() + "");
        comment.setUpdateTime(new Date());
        comment.setCreateTime(new Date());
        comment.setUserAvatar(user.getAvatar());
        comment.setUserName(user.getUserName());
        comment.setUserId(user.getId());
        //保存入库
        commentDao.save(comment);
        //TODO：发送通知-->通过邮件
        //返回结果
        return ResponseResult.SUCCESS("评论成功");
    }

    /**
     * 获取文章的评论
     * 评论的排序策略：
     * 最基本的j就是按时间排序-->升序和降序-->先发表的在前面或者后发表的在前
     * <p>
     * 置顶的一定在最前面
     * <p>
     * 后发表的：前单位时间内会排在前面，过了此单位时间，会按点赞量和发表时间进行排序
     *
     * @param articleId
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listCommentByArticleId(String articleId, int page, int size) {
        page = checkPage(page);
        size = checkSize(size);
        Sort sort=Sort.by(Sort.Direction.DESC,"state","create_time");
        Pageable pageable = PageRequest.of(page - 1, size,sort);
        Page<Comment> all = commentDao.findAll(pageable);
        ResponseResult success = ResponseResult.SUCCESS("文章评论列表获取成功");
        success.setData(all);
        return success;
    }

    @Override
    public ResponseResult deleteCommentById(String commentId) {
        //检查用户角色
        User user = userService.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //把评论找出来，比对用户权限
        Comment comment = commentDao.findOneById(commentId);
        if (comment == null) {
            return ResponseResult.FAILED("评论不存在");
        }
        //用户id不一样，只有管理员可以删
        //登录了要判断角色
        if (Constants.user.ROLE_ADMIN.equals(user.getRole())||user.getId().equals(comment.getUserId())) {
            commentDao.deleteById(commentId);
            return ResponseResult.SUCCESS("评论删除成功");
        } else {
            return ResponseResult.PERMISSION_FORBID();
        }
    }

    @Override
    public ResponseResult listComments(int page, int size) {
        page = checkPage(page);
        size = checkSize(size);
        Sort sort = Sort.by(Sort.Direction.DESC,"create_time");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<Comment> all = commentDao.findAll(pageable);
        ResponseResult success = ResponseResult.SUCCESS("获取评论列表成功");
        success.setData(all);
        return success;
    }

    @Override
    public ResponseResult topComment(String commentId) {
        Comment comment = commentDao.findOneById(commentId);
        if (comment==null){

            return ResponseResult.FAILED("评论不存在");
        }
        String state = comment.getState();
        if (Constants.Comment.STATE_PUBLISH.equals(state)) {
            comment.setState(Constants.Comment.STATE_TOP);
            return ResponseResult.SUCCESS("置顶成功");
        }else if (Constants.Comment.STATE_TOP.equals(state)){
            comment.setState(Constants.Comment.STATE_PUBLISH);
            return ResponseResult.SUCCESS("取消置顶");
        }else {
            return ResponseResult.FAILED("评论状态非法");
        }
    }
}
