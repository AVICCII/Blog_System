package com.aviccii.cc.services.impl;

import com.aviccii.cc.dao.ImageDao;
import com.aviccii.cc.pojo.Image;
import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import com.aviccii.cc.services.IImageService;
import com.aviccii.cc.services.IUserService;
import com.aviccii.cc.utils.Constants;
import com.aviccii.cc.utils.IdWorker;
import com.aviccii.cc.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aviccii 2020/10/19
 * @Discrimination
 */
@Slf4j
@Service
@Transactional
public class IImageServiceImpl extends BaseSerive implements IImageService {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

    @Value("${blog.image.save-Path}")
    public  String imagePath;

    @Value("${blog.image.max-size}")
    public  long imageMaxSize;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IUserService userService;

    @Autowired
    private ImageDao imageDao;
    /**
     * 上传的路径：可以配置，在配置文件里配置
     * 上传的内容，命名--》可以用id --》每天一个文件夹保存
     * 限制文件大小，通过配置文件配置
     * 保存记录到数据库里
     * ID | 存储路径 | url | 原名称 | 状态 |创建日期 | 更新日期
     * @param file
     * @return
     */
    @Override
    public ResponseResult uploadImage(MultipartFile file) {
        //判断是否有文件
        if (file == null) {
            return ResponseResult.FAILED("图片不可以为空");
        }
        //判断文件类型，我们只支持图片上传，例如png,jpg,gif
        String contentType = file.getContentType();
        log.info("contentType == >" + contentType);
        if (TextUtils.isEmpty(contentType)) {
            return ResponseResult.FAILED("文件格式错误。");
        }
        //获取相关数据，比如说文件类型，文件名称
        String originalFilename = file.getOriginalFilename();
        String type = getType(contentType, originalFilename);
        if (type==null) {
            ResponseResult.FAILED("不支持此文件类型");
        }
        System.out.println(type);
        log.info("originalFilename == > " + originalFilename);
        //限制文件的大小
        long size = file.getSize();
        log.info("maxSize === >" +imageMaxSize +" size === > "+size);
        if (size>imageMaxSize){
            return ResponseResult.FAILED("图片最大仅支持"+(imageMaxSize/1024/1024)+"Mb");
        }
        //创建图片的保存目录
        //规则：配置目录/日期/类型/ID.类型
        long currentMilliseconds = System.currentTimeMillis();
        String currentDay = simpleDateFormat.format(currentMilliseconds);
        log.info("current day == >" + currentDay);
        String dayPath = imagePath+File.separator+currentDay;
        File dayPathFile = new File(dayPath);
        //判断日期文件夹是否存在
        if (!dayPathFile.exists()){
            dayPathFile.mkdirs();
        }
        String targetName = String.valueOf(idWorker.nextId());
        String targetPath = dayPath+File.separator+type+File.separator+targetName+"."+type;
        System.out.println(targetPath);
        File targetFile = new File(targetPath);
        //判断类型文件夹是否存在
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            log.info("targetFile == >" + targetFile);
            //保存文件
            file.transferTo(targetFile);

            //第一个是返回路径 --> 得对应着解析来
            Map<String,String> result = new HashMap<>();
            String resultPath = currentMilliseconds + "_" + targetName +"."+type;
            result.put("path",resultPath);
            //第二个是名称--->alt="图片描述"，如果不写，前端可以使用名称作为这个描述
            result.put("name",originalFilename);
            //记录文件
            Image image = new Image();
            image.setContentType(contentType);
            image.setId(targetName);
            image.setCreateTime(new Date());
            image.setUpdateTime(new Date());
            image.setPath(targetFile.getPath());
            image.setName(originalFilename);
            image.setUrl(resultPath);
            image.setState("1");
            User user = userService.checkUser();
            image.setUserId(user.getId());
            //保存记录到数据库里
            imageDao.save(image);
            //TODO:
            //返回结果：包含这个图片的名称和访问路径
            ResponseResult success = ResponseResult.SUCCESS("文件上传成功");
            success.setData(result);
            return success;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //记录文件
        //返回结果
        return ResponseResult.FAILED("上传失败,请稍后重试");
    }

    private String getType(String contentType, String name) {
        String type = null;
        if (contentType.equals(Constants.imageType.TYPE_PNG_WITH_PREFIX)
                &&name.endsWith(Constants.imageType.TYPE_PNG)){
            type=Constants.imageType.TYPE_PNG;
        }else if (Constants.imageType.TYPE_GIF_WITH_PREFIX.equals(contentType)
                &&name.endsWith(Constants.imageType.TYPE_GIF)){
            type=Constants.imageType.TYPE_GIF;
        }else if (Constants.imageType.TYPE_JPG_WITH_PREFIX.equals(contentType)
                &&name.endsWith(Constants.imageType.TYPE_JPG)){
            type=Constants.imageType.TYPE_JPG;
        }else if (Constants.imageType.TYPE_JPEG_WITH_PREFIX.equals(contentType)
                &&name.endsWith(Constants.imageType.TYPE_JPG)){
            type=Constants.imageType.TYPE_JPG;
        }
        return type;
    }

    @Override
    public void viewImage(HttpServletResponse response, String imageId) throws IOException {
        //配置的目录已知
        //根据尺寸来动态返回图片给前端
        //好处：减少带宽占用，传输速度快
        //缺点：消耗后台cpu资源
        //推荐做法：上传上来的时候，把图片复制成三个尺寸：大中小
        //根据尺寸范围，返回结果即可
        //需要日期
        String[] paths = imageId.split("_");
        String dayValue = paths[0];
        String format = simpleDateFormat.format(Long.parseLong(dayValue));
        log.info("viewImage format == >"+format);
        //ID
        String name = paths[1];
        //需要类型
        String type = name.substring(name.length() - 3);
        //使用日期的时间戳_ID.类型
        String targetPath = imagePath + File.separator + format+File.separator +type+File.separator+
                File.separator+name;
        log.info("get image target path  === >" +targetPath);

        File file = new File(targetPath);
        OutputStream writer = null;
        FileInputStream fos = null;
        try {
            response.setContentType("image/jpg");
            writer = response.getOutputStream();
            //读取
            fos = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int length;
            while ((length = fos.read(buff)) != -1) {
                writer.write(buff, 0, length);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (writer != null) {
                writer.close();
            }
        }

    }

    @Override
    public ResponseResult listImages(int page, int size) {
        //处理page和size
        page = checkPage(page);
        size = checkSize(size);
        User user = userService.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //创建分页条件
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        //查询
        Pageable pageable = PageRequest.of(page-1,size,sort);
        //返回结果
        final String userId = user.getId();
        Page<Image> all = imageDao.findAll(new Specification<Image>() {
            @Override
            public Predicate toPredicate(Root<Image> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //根据用户id
                Predicate userIdPre = criteriaBuilder.equal(root.get("userId").as(String.class),userId);
                //根据状态
                Predicate statePre = criteriaBuilder.equal(root.get("state").as(String.class), "1");
                return criteriaBuilder.and(userIdPre,statePre);
            }
        },pageable);

        ResponseResult success = ResponseResult.SUCCESS("获取图片列表成功");
        success.setData(all);
        return success;
    }

    /**
     * 删除图片，只改变状态
     * @param imageId
     * @return
     */
    @Override
    public ResponseResult deleteById(String imageId) {
        int result = imageDao.deleteImageByUpdateState(imageId);
        if (result>0){
            return ResponseResult.SUCCESS("删除成功");
        }
        return ResponseResult.FAILED("图片不存在");
    }
}
