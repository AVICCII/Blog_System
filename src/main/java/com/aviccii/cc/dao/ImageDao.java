package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author aviccii 2020/10/20
 * @Discrimination
 */
public interface ImageDao extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {

    @Modifying
    @Query(nativeQuery = true, value = "update `tb_images` set `state` = '0' where id =? ")
    int deleteImageByUpdateState(String imageId);
}
