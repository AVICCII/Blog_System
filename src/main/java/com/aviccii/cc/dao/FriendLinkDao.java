package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author aviccii 2020/10/16
 * @Discrimination
 */
public interface FriendLinkDao extends JpaRepository<FriendLink,String>, JpaSpecificationExecutor<FriendLink> {
    FriendLink findOneById(String friendLinkId);

    int deleteAllById(String friendLinkId);

}
