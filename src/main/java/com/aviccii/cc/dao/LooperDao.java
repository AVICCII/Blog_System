package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Looper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author aviccii 2020/10/20
 * @Discrimination
 */
public interface LooperDao extends JpaRepository<Looper,String>,JpaSpecificationExecutor<Looper> {

    Looper findOneById(String loopId);
}
