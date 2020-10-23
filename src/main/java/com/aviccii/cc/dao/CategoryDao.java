package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author aviccii 2020/10/15
 * @Discrimination
 */
public interface CategoryDao extends JpaRepository<Category,String>, JpaSpecificationExecutor<Category> {
    Category findOneById(String categoryId);


    @Modifying
    @Query(nativeQuery = true,value = "update `tb_categories` set `status` = '0' where `id` = ?")
    int deleteCategoryByUpdateState(String categoryId);

    @Query(nativeQuery = true,value = "select * from `tb_categories` where `status`=? order by `create_time` DESC")
    List<Category> listCategoriesByState(String status);
}
