package com.aviccii.cc.controller;

import com.aviccii.cc.dao.CommentDao;
import com.aviccii.cc.dao.LabelDao;
import com.aviccii.cc.pojo.Comment;
import com.aviccii.cc.pojo.Label;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.*;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Date;
import java.util.List;

import static com.aviccii.cc.utils.Constants.DEFAULT_SIZE;

/**
 * @author aviccii 2020/9/15
 * @Discrimination
 */
//@Controller
//@ResponseBody  13+14代码等效16
@Transactional
@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private LabelDao labelDao;

    public static final Logger log  = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String helloWorld(){
        log.info("hello,world...");
        String captchaContent = (String) redisUtil.get(Constants.user.KEY_CAPTCHA_CONTENT + "123456");
        log.info("captchaContent == >"+captchaContent);
        return "hello world!";
    }

    @PostMapping("/label")
    public ResponseResult addLabel(@RequestBody Label label){
        //判断数据是否有效
        //补全数据
        label.setId(idWorker.nextId()+"");
        label.setCreateTime(new Date());
        label.setUpdate_time(new Date());
        //保存数据
        labelDao.save(label);
        return ResponseResult.SUCCESS("添加成功");
    }


    @DeleteMapping("/label/{labelId}")
    public ResponseResult delete(@PathVariable("labelId")String labelId){
        labelDao.deleteById(labelId);
        return ResponseResult.SUCCESS("删除标签成功");
    }

    @PutMapping("/label/{labelId}")
    public ResponseResult updateLabel(@PathVariable("labelId")String labelId,@RequestBody Label label){
        Label dblabel = labelDao.findOneByid(labelId);
        if (dblabel == null){
            return ResponseResult.FAILED("标签不存在");
        }
        dblabel.setCount(label.getCount());
        dblabel.setName(label.getName());
        dblabel.setUpdate_time(new Date());
        labelDao.save(dblabel);
        return ResponseResult.SUCCESS("修改成功");
    }

    @GetMapping("/label/{labelId}")
    public ResponseResult getLabelById(@PathVariable("labelId")String labelId){
        Label oneByid = labelDao.findOneByid(labelId);
        if (oneByid == null){
            return ResponseResult.FAILED("标签不存在");
        }
        ResponseResult success = ResponseResult.SUCCESS("查询成功");
        success.setData(oneByid);
        return success;
    }


    @GetMapping("/label/list/{page}/{size}")
    public ResponseResult listlabels(@PathVariable("page")int page,@PathVariable("size")int size){
        if (page<1){
            page = 1;
        }
        if (size <=0 ){
            size =DEFAULT_SIZE;
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<Label> all = labelDao.findAll(pageable);
        ResponseResult success = ResponseResult.SUCCESS("获取成功");
        success.setData(all);
        return success;
    }

    @GetMapping("/label/search")
    public ResponseResult doLabelSearch(@RequestParam("keyword")String keyword,@RequestParam("count")int count){
        List<Label> all = labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate name = cb.like(root.get("name").as(String.class), "%"+keyword+"%");
                Predicate countpre = cb.equal(root.get("count").as(Integer.class), count);
                Predicate predicate = cb.and(name, countpre);
                return predicate;
            }
        });
        if (all.size()== 0){
            return ResponseResult.FAILED("结果为空");
        }
        ResponseResult success = ResponseResult.SUCCESS("查找成功");
        success.setData(all);
        return success;
    }

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setContentType("image/gif");
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);

        //三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130,48,5);
        //设置字体
        specCaptcha.setFont(new Font("Verdana",Font.PLAIN,32));
        //设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_CHAR);

        String content = specCaptcha.text().toLowerCase();
        //验证码存入session
//        request.getSession().setAttribute("captcha",specCaptcha.text().toLowerCase());
        //保存到redis中 10分钟有效
        redisUtil.set(Constants.user.KEY_CAPTCHA_CONTENT +"123456",content,600);
        //输出图片流
        specCaptcha.out(response.getOutputStream());

    }

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IUserService userService;

    @PostMapping("/comment")
    public ResponseResult testComment(@RequestBody Comment comment,HttpServletRequest request,HttpServletResponse response){
        String content = comment.getContent();
        log.info("comment content ===>" +content);
        //还得知道是谁的评论，对这个评论，身份进行确定
        String tokenKey = CookieUtils.getCookie(request, Constants.user.COOKIE_TOKEN_KEY);
        if (tokenKey==null){
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }


        User user = userService.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        comment.setUserId(user.getId());
        comment.setUserAvatar(user.getAvatar());
        comment.setUserName(user.getUserName());
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setId(idWorker.nextId()+"");
        commentDao.save(comment);
        return ResponseResult.SUCCESS("评论成功");
    }


}
