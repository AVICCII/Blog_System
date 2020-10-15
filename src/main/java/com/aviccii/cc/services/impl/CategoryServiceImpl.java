package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.CategoryDao;
import com.aviccii.cc.pojo.Category;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ICategoryService;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author aviccii 2020/10/15
 * @Discrimination
 */
@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoryDao categoryDao;

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
}
