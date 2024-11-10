package com.lynnwork.sobblogsystem.service.impl;

import com.lynnwork.sobblogsystem.pojo.Image;
import com.lynnwork.sobblogsystem.mapper.ImageMapper;
import com.lynnwork.sobblogsystem.service.IImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * tb_image 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-11-04
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService {

}
