package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.CategoryDao;
import com.aviccii.cc.pojo.Category;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ICategoryService;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author aviccii 2020/10/15
 * @Discrimination
 */
@Service
@Transactional
public class CategoryServiceImpl extends BaseSerive implements ICategoryService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private IUserService iUserService;

    @Override
    public ResponseResult addCategory(Category category) {
        //先检查数据
        //必须要的数据：分类名称，拼音，顺序，描述
        if (TextUtils.isEmpty(category.getCategoryName())) {
            return ResponseResult.FAILED("分类名称不可为空");
        }
        if (TextUtils.isEmpty(category.getCategoryPy())) {
            return ResponseResult.FAILED("分类拼音不可为空");
        }
        if (TextUtils.isEmpty(category.getDescription())) {
            return ResponseResult.FAILED("分类描述不可为空");
        }
        //补全数据
        category.setId(idWorker.nextId()+"");
        category.setStatus("1");
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        //保存数据
        categoryDao.save(category);
        //返回结果
        return ResponseResult.SUCCESS("添加分类成功");
    }

    @Override
    public ResponseResult getCategory(String categoryId) {
        Category category = categoryDao.findOneById(categoryId);
        if (category == null) {
            return ResponseResult.FAILED("分类不存在");
        }
        ResponseResult success = ResponseResult.SUCCESS("获取分类成功");
        success.setData(category);
        return success;
    }

    @Override
    public ResponseResult listCategories() {
        //参数检查
        //创建条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime","order");

        //判断用户角色  普通用户或者未登录用户只能获取正常的category
        //管理员账户可以拿到所有
        User user = iUserService.checkUser();
        List<Category> categories ;
        if (user == null||!Constants.user.ROLE_ADMIN.equals(user.getRole())) {
            //只能获取到正常的category
            categories = categoryDao.listCategoriesByState("1");
        }else {
            //查询
            categories = categoryDao.findAll(sort);
        }

        //返回结果
        ResponseResult success = ResponseResult.SUCCESS("获取分类列表成功");
        success.setData(categories);
        return success;
    }

    @Override
    public ResponseResult updateCategory(String categoryId, Category category) {
        //第一步是找出来
        Category categoryFromDb = categoryDao.findOneById(categoryId);
        if (categoryFromDb == null) {
            return ResponseResult.FAILED("分类不存在");
        }
        //第二步是对内容进行判断，有些字段是不为空的
        String name = category.getCategoryName();
        if (!TextUtils.isEmpty(name)) {
            categoryFromDb.setCategoryName(name);
        }
        String py = category.getCategoryPy();
        if (!TextUtils.isEmpty(py)) {
            categoryFromDb.setCategoryPy(py);
        }
        String description = category.getDescription();
        if (!TextUtils.isEmpty(description)) {
            categoryFromDb.setDescription(description);
        }
        categoryFromDb.setStatus(category.getStatus());
        categoryFromDb.setOrder(category.getOrder());
        categoryFromDb.setUpdateTime(new Date());
        //第三步是保存数据
        categoryDao.save(categoryFromDb);
        //返回结果
        ResponseResult success = ResponseResult.SUCCESS("分类更新成功");
        success.setData(category);
        return success;
    }

    @Override
    public ResponseResult deleteCategory(String categoryId) {
        int result = categoryDao.deleteCategoryByUpdateState(categoryId);
        if (result==0){
            return ResponseResult.FAILED("该分类不存在");
        }
        return ResponseResult.SUCCESS("删除分类成功");
    }
}
