package com.aviccii.cc.controller.admin;

import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
@RestController
@RequestMapping("/admin/image")
public class ImageAdminApi {

    @PostMapping
    public ResponseResult uploadImage(){
        return null;
    }

    @DeleteMapping("/{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId")String imageId){
        return null;
    }

    @PutMapping("/{imageId}")
    public ResponseResult uploadImage(@PathVariable("imageId")String imageId){
        return null;
    }

    @GetMapping("/{imageId}")
    public ResponseResult getImage(@PathVariable("imageId")String imageId){
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listImages(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }
}
