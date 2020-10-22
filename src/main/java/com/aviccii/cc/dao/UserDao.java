package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.User;
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

    /**
     * 通过修改用户的状态来修改用户
     * @param userId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_user` set `state` = '0' where `id` = ?")
    int deleteUserByState(String userId);

    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_user` set `password` = ? where `email` = ?")
    int updatePasswordByEmail(String encode, String email);

    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_user` set `email` = ? where `id` = ? ")
    int updateEmailById(String email, String id);
}
