package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.User;
import com.aviccii.cc.pojo.UserNoPassword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
public interface UserNoPasswordDao extends JpaRepository<UserNoPassword,String>, JpaSpecificationExecutor<UserNoPassword> {

}
