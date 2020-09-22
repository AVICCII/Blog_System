package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author aviccii 2020/9/22
 * @Discrimination
 */
public interface LabelDao extends JpaRepository<Label,String>, JpaSpecificationExecutor<Label> {

    Label findOneByid(String id);

    Label findOneByName(String name);
}
