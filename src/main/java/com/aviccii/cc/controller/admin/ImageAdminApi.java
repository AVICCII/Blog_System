package com.aviccii.cc.controller.admin;

import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/image")
public class ImageAdminApi {

    @Autowired
    private IImageService iImageService;

    /**
     * 关于图片上传
     * 一般来说，现在比较常用的是对象存储-->很简单，看文档就可以学会
     * 使用Nginx+fastDFS ==> fastDFS-->处理文件上传 Nginx-->负责文件访问
     */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult uploadImage(@RequestParam("file")MultipartFile file){
        return iImageService.uploadImage(file);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId")String imageId){
        return null;
    }


    @PreAuthorize("@permission.admin()")
    @GetMapping("/{imageId}")
    public void getImage(HttpServletResponse response, @PathVariable("imageId")String imageId){
        try {
            iImageService.viewImage(response,imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listImages(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }
}
