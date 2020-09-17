package com.aviccii.cc.controller.admin;

import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/17
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminApi {

    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){
        return null;
    }

    @DeleteMapping("/{categoryId}")
    public ResponseResult deleteCategory(@PathVariable("categoryId")String categoryId){
        return null;
    }

    @PutMapping("/{categoryId}")
    public ResponseResult updateCategory(@PathVariable("categoryId")String categoryId,@RequestBody Category category){
        return null;
    }

    @GetMapping("/{categoryId}")
    public ResponseResult getCategory(@PathVariable("categoryId")String categoryId){
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listCategories(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }

}
