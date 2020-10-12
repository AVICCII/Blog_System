package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author aviccii 2020/10/12
 * @Discrimination
 */
public interface RefreshTokenDao extends JpaRepository<RefreshToken,String>, JpaSpecificationExecutor<RefreshToken> {


    RefreshToken findOneByTokenKey(String tokenKey);

    int deleteAllByUserId(String userId);
}
