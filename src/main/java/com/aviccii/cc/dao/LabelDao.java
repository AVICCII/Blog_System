package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author aviccii 2020/9/22
 * @Discrimination
 */
public interface LabelDao extends JpaRepository<Label,String>, JpaSpecificationExecutor<Label> {

    Label findOneByid(String id);

    Label findOneByName(String name);

    @Modifying
    @Query(nativeQuery = true,value = "update `tb_labels` set `count` = `count` + 1 where `name`=?")
    int updateCountByName(String labelName);
}
