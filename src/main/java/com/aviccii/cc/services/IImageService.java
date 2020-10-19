package com.aviccii.cc.services;

import com.aviccii.cc.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aviccii 2020/10/19
 * @Discrimination
 */
public interface IImageService {
    ResponseResult uploadImage(MultipartFile file);

    void viewImage(HttpServletResponse response, String imageId) throws IOException;
}
