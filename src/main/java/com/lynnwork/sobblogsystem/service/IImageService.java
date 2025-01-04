package com.lynnwork.sobblogsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lynnwork.sobblogsystem.pojo.Image;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * tb_image 服务类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-11-04
 */
public interface IImageService extends IService<Image> {

    ResponseResult uploadImage(MultipartFile file);

    void getImage(String url, HttpServletResponse resp) throws IOException;
}
