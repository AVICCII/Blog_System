package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.SettingDao;
import com.aviccii.cc.dao.SettingsDao;
import com.aviccii.cc.pojo.Setting;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IWebSizeInfoService;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.aviccii.cc.utils.Constants.settings.*;

/**
 * @author aviccii 2020/10/21
 * @Discrimination
 */
@Service
@Transactional
public class WebSizeInfoImpl extends BaseSerive implements IWebSizeInfoService {

    @Autowired
    private SettingDao settingDao;

    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult getWebSizeTitle() {
        Setting title = settingDao.findOneByKey(WEB_SIZE_TITLE);
        ResponseResult success = ResponseResult.SUCCESS("获取网站title成功");
        success.setData(title);
        return success;
    }

    @Override
    public ResponseResult putWebSizeTitle(String title) {
        if (TextUtils.isEmpty(title)){
            return ResponseResult.FAILED("网站标题不可以为空");
        }
        Setting titleFromDb = settingDao.findOneByKey(WEB_SIZE_TITLE);
        if (titleFromDb == null) {
            titleFromDb = new Setting();
            titleFromDb.setId(idWorker.nextId()+"");
            titleFromDb.setUpdate_time(new Date());
            titleFromDb.setCreate_time(new Date());
            titleFromDb.setKey(WEB_SIZE_TITLE);
        }
        titleFromDb.setValue(title);
        settingDao.save(titleFromDb);
        return ResponseResult.SUCCESS("网站Title更新成功");
    }

    @Override
    public ResponseResult getSeoInfo() {
        Setting description = settingDao.findOneByKey(WEB_SIZE_DESCRIPTION);
        Setting keywords = settingDao.findOneByKey(WEB_SIZE_KEYWORDS);
        Map<String,String> result = new HashMap<>();
        result.put(description.getKey(),description.getValue());
        result.put(keywords.getKey(),keywords.getValue());
        ResponseResult success = ResponseResult.SUCCESS("获取SEO信息成功");
        success.setData(result);
        return success;
    }

    @Override
    public ResponseResult putSeoInfo(String keywords, String description) {
        //判断
        if (TextUtils.isEmpty(keywords)){
            return ResponseResult.FAILED("关键字不可为空");
        }
        if (TextUtils.isEmpty(description)){
            return ResponseResult.FAILED("描述不可为空");
        }
        Setting descriptionFromDb = settingDao.findOneByKey(WEB_SIZE_DESCRIPTION);
        if (descriptionFromDb == null) {
            descriptionFromDb = new Setting();
            descriptionFromDb.setId(idWorker.nextId()+"");
            descriptionFromDb.setCreate_time(new Date());
            descriptionFromDb.setUpdate_time(new Date());
            descriptionFromDb.setKey(WEB_SIZE_DESCRIPTION);

        }
        descriptionFromDb.setValue(description);
        settingDao.save(descriptionFromDb);
        Setting keywordsFromDb = settingDao.findOneByKey(WEB_SIZE_KEYWORDS);
        if (keywordsFromDb == null) {
            keywordsFromDb = new Setting();
            keywordsFromDb.setId(idWorker.nextId()+"");
            keywordsFromDb.setCreate_time(new Date());
            keywordsFromDb.setUpdate_time(new Date());
            keywordsFromDb.setKey(WEB_SIZE_KEYWORDS);
        }
        keywordsFromDb.setValue(keywords);
        settingDao.save(keywordsFromDb);
        return ResponseResult.SUCCESS("更新seo信息成功");
    }

    /**
     * 这个是全网站的访问量，要做的细一点需要分来源
     * 这里只统计浏览量，只统计文章的浏览量，提供一个浏览量的统计接口(页面级）
     * @return
     */
    @Override
    public ResponseResult getSizeViewCount() {
        Setting descriptionFromDb = settingDao.findOneByKey(WEB_SIZE_VIEW_COUNT);
        if (descriptionFromDb == null) {
            descriptionFromDb = new Setting();
            descriptionFromDb.setId(idWorker.nextId()+"");
            descriptionFromDb.setKey(WEB_SIZE_VIEW_COUNT);
            descriptionFromDb.setUpdate_time(new Date());
            descriptionFromDb.setCreate_time(new Date());
            descriptionFromDb.setValue("1");
            settingDao.save(descriptionFromDb);
        }
        Map<String, Integer> result = new HashMap<>();
        result.put(descriptionFromDb.getKey(),Integer.valueOf(descriptionFromDb.getValue()));
        ResponseResult success = ResponseResult.SUCCESS("获取网站浏览量成功");
        success.setData(result);
        return success;
    }
}
