package com.aviccii.cc.controller.portal;

import com.aviccii.cc.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aviccii 2020/10/27
 * @Discrimination
 */
@RestController
@RequestMapping("/portal/image")
public class ImagePortalApi {

    @Autowired
    private IImageService iImageService;

    @GetMapping("/{imageId}")
    public void getImage(HttpServletResponse response, @PathVariable("imageId")String imageId){
        try {
            iImageService.viewImage(response,imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
