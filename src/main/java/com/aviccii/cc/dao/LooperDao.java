package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Looper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author aviccii 2020/10/20
 * @Discrimination
 */
public interface LooperDao extends JpaRepository<Looper,String>,JpaSpecificationExecutor<Looper> {

    Looper findOneById(String loopId);

    @Query(nativeQuery = true,value = "select * from `tb_looper` where `state` = ?")
    List<Looper> listLooperByState(String s);
}
