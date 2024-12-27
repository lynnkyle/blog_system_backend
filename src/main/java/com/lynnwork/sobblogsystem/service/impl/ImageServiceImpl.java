package com.lynnwork.sobblogsystem.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.mapper.ImageMapper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IImageService;
import com.lynnwork.sobblogsystem.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;

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

    @Override
    public ResponseResult uploadImage(MultipartFile file) {
        //1.检查文件(文件判空、文件类型、文件名称)
        if (file == null) {
            return ResponseResult.FAILED("图片不可以为空。");
        }
        String type = file.getContentType();
        if (TextUtil.isEmpty(type)) {
            return ResponseResult.FAILED("文件格式错误。");
        }
        if (!"image/jpeg".equals(type) && !"image/png".equals(type) && !"image/gif".equals(type)) {
            return ResponseResult.FAILED("不支持此文件类型。");
        }
        //2.保存文件
        String originalFileName = file.getOriginalFilename();
        File targetFile = new File(imagePath + File.separator + originalFileName);
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseResult.FAILED("图片上传失败，请稍后重试。");
        }
        return ResponseResult.SUCCESS("文件上传成功");
    }

    @Override
    public void getImage(String imageId, HttpServletResponse resp) throws IOException {
        //
        resp.setContentType("image/jpeg");
        File file = new File(imagePath + File.separator + "avatar.jpg");
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
}
