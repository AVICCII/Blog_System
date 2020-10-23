package com.aviccii.cc.services;

import com.aviccii.cc.pojo.Category;
import com.aviccii.cc.response.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author aviccii 2020/10/15
 * @Discrimination
 */

public interface ICategoryService {
    ResponseResult addCategory(Category category);

    ResponseResult getCategory(String categoryId);

    ResponseResult listCategories();

    ResponseResult updateCategory(String categoryId, Category category);

    ResponseResult deleteCategory(String categoryId);

}
