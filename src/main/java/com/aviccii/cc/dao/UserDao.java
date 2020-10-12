package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查找
     * @param userName
     * @return
     */
    User findOneByUserName(String userName);

    /**
     * 根据邮箱查找
     * @param email
     * @return
     */
    User findOneByEmail(String email);

    List<User> findOneByEmailOrUserName(String email, String userName);

    /**
     * 根据UserId 查找用户
     * @param UserId
     * @return
     */
    User findOneById(String UserId);
}
