package com.aviccii.cc.dao;

import com.aviccii.cc.pojo.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
public interface SettingsDao extends JpaRepository<Setting,String>, JpaSpecificationExecutor<Setting> {

    Setting findOneByKey(String key);
}
