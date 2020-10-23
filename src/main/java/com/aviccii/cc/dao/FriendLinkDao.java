package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author aviccii 2020/10/16
 * @Discrimination
 */
public interface FriendLinkDao extends JpaRepository<FriendLink,String>, JpaSpecificationExecutor<FriendLink> {
    FriendLink findOneById(String friendLinkId);

    int deleteAllById(String friendLinkId);

    @Modifying
    @Query(value = "select * from `tb_friends` where `state` = ?",nativeQuery = true)
    List<FriendLink> listfriendLinkByState(String s);
}
