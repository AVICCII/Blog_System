package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

}
