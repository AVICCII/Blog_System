package com.aviccii.cc.controller.admin;

import com.aviccii.cc.pojo.Category;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/17
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminApi {

    @Autowired
    private ICategoryService iCategoryService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){
        return iCategoryService.addCategory(category);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseResult deleteCategory(@PathVariable("categoryId")String categoryId){
        return null;
    }

    @PutMapping("/{categoryId}")
    public ResponseResult updateCategory(@PathVariable("categoryId")String categoryId,@RequestBody Category category){
        return null;
    }

    /**
     * 获取分类
     * 修改的时候，获取一下，填充弹窗
     * 不获取也是可以的，从列表中获取数据
     *
     * 权限：管理员
     * @param categoryId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{categoryId}")
    public ResponseResult getCategory(@PathVariable("categoryId")String categoryId){
        return iCategoryService.getCategory(categoryId);
    }

    @GetMapping("/list")
    public ResponseResult listCategories(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }

}
