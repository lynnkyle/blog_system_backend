package com.lynnwork.sobblogsystem.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.mapper.ImageMapper;
import com.lynnwork.sobblogsystem.pojo.Image;
import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IImageService;
import com.lynnwork.sobblogsystem.service.IUserService;
import com.lynnwork.sobblogsystem.utils.Constants;
import com.lynnwork.sobblogsystem.utils.SnowflakeIdWorker;
import com.lynnwork.sobblogsystem.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.URLConnection;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * tb_image 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-11-04
 */
@Slf4j
@Service
@Transactional
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService {

    @Value("${sob.blog.system.image.save-path}")
    private String imagePath;

    @Value("${sob.blog.system.image.max-size}")
    private long maxSize;

    @Autowired
    private SnowflakeIdWorker idWorker;

    @Autowired
    private IUserService userService;
    @Autowired
    private ImageMapper imageMapper;

    /*
       文件上传路径:(配置目录/日期/类型/ID.类型)
     */
    @Override
    public ResponseResult uploadImage(MultipartFile file) {
        //1.检查文件(文件判空、文件类型，文件名称、文件大小)
        if (file == null) {
            return ResponseResult.FAILED("图片不可以为空。");
        }
        // 检查文件类型
        String contentType = file.getContentType();
        if (TextUtil.isEmpty(contentType)) {
            return ResponseResult.FAILED("图片格式错误。");
        }
        String originalFileName = file.getOriginalFilename();
        String type = getType(contentType, originalFileName);
        if (type == null) {
            return ResponseResult.FAILED("不支持此图片类型。");
        }
        // 限制文件大小
        long size = file.getSize();
        if (size > maxSize) {
            return ResponseResult.FAILED("图片最大仅支持" + (maxSize / 1024 / 1024) + "MB");
        }
        //2.保存文件(文件+数据库记录)  dayPath: 保存路径
        // 保存文件到指定目录
        long currentMillions = System.currentTimeMillis();
        String currentDate = new SimpleDateFormat("yyyy_MM_dd").format(new Date(currentMillions));
        String dayPath = imagePath + File.separator + currentDate;
        File dayFile = new File(dayPath);
        if (!dayFile.exists()) {    //判断日期文件夹是否存在
            dayFile.mkdirs();
        }
        String targetName = String.valueOf(idWorker.nextId());
        String targetPath = dayPath + File.separator + type + File.separator + targetName + "." + type;
        File targetFile = new File(targetPath);
        if (!targetFile.getParentFile().exists()) { //判断类型文件夹是否存在
            targetFile.getParentFile().mkdirs();
        }
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            file.transferTo(targetFile);
            // 保存文件记录(1.访问路径Url(resultPath);2.保存路径Path(targetPath))到数据库
            Map<String, String> result = new HashMap<>();
            String url = currentMillions + "_" + targetName + "." + type;
            result.put("url", url);
            result.put("alt", originalFileName);
            Image image = new Image();
            image.setId(targetName);
            image.setName(originalFileName);
            image.setUrl(url);
            image.setPath(targetPath);
            User user = userService.checkUser();
            image.setUserId(user.getId());
            image.setState(Constants.Image.DEFAULT_STATE);
            image.setContentType(contentType);
            image.setCreateTime(new Date());
            image.setUpdateTime(new Date());
            imageMapper.insert(image);
            return ResponseResult.SUCCESS("文件上传成功。").setData(result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseResult.FAILED("图片上传失败，请稍后重试。");
    }

    /*
       获取图片(配置的目录(已知)、日期(未知、时间戳)、类型(未知)、ID(未知))
       图片的Url格式:(配置目录/日期/类型/ID.类型)
       根据尺寸(大、中、小)动态返回图片给前端
       好处: 减少带宽占用, 传输速度快
       缺点: 消耗后台cpu资源
     */
    @Override
    public void getImage(String url, HttpServletResponse resp) throws IOException {
        // 1.设置response响应头ContentType
        String contentType = URLConnection.guessContentTypeFromName(url);
        resp.setContentType(contentType);
        // 2.处理访问路径
        String[] paths = url.split("_");
        String millions = paths[0];
        String datePath = new SimpleDateFormat("yyyy_MM_dd").format(new Date(Long.parseLong(millions)));
        String name = paths[1];
        log.info("contentType{}", contentType);
        String type = getType(contentType);
        String targetPath = imagePath + File.separator + datePath + File.separator + type + File.separator + name;
        // 3.将目标文件写入response的响应体中
        File file = new File(targetPath);
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        OutputStream fos = null;
        try {
            fis = new FileInputStream(file);
            fos = resp.getOutputStream();
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    public String getType(String contentType) {
        String type = null;
        if (Constants.ImageType.TYPE_JPEG_WITH_PREFIX.equals(contentType)) {
            type = Constants.ImageType.TYPE_JPEG;
        } else if (Constants.ImageType.TYPE_PNG_WITH_PREFIX.equals(contentType)) {
            type = Constants.ImageType.TYPE_PNG;
        } else if (Constants.ImageType.TYPE_GIF_WITH_PREFIX.equals(contentType)) {
            type = Constants.ImageType.TYPE_GIF;
        }
        return type;
    }

    /*

     */
    private String getType(String contentType, String name) {
        String type = null;
        if (Constants.ImageType.TYPE_JPEG_WITH_PREFIX.equals(contentType) && (name.endsWith(Constants.ImageType.TYPE_JPG) || name.endsWith(Constants.ImageType.TYPE_JPEG))) {
            type = Constants.ImageType.TYPE_JPEG;
        } else if (Constants.ImageType.TYPE_PNG_WITH_PREFIX.equals(contentType) && name.endsWith(Constants.ImageType.TYPE_PNG)) {
            type = Constants.ImageType.TYPE_PNG;
        } else if (Constants.ImageType.TYPE_GIF_WITH_PREFIX.equals(contentType) && name.endsWith(Constants.ImageType.TYPE_GIF)) {
            type = Constants.ImageType.TYPE_GIF;
        }
        return type;
    }
}
