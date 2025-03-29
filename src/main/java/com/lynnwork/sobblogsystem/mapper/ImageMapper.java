package com.lynnwork.sobblogsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lynnwork.sobblogsystem.pojo.Image;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * tb_image Mapper 接口
 * </p>
 *
 * @author lynnkyle
 * @since 2024-11-04
 */
public interface ImageMapper extends BaseMapper<Image> {

    IPage<Image> selectPageVo(IPage<Image> iPage, @Param("userId") String userId);

    int deleteImageByState(@Param("id") String imageId);
}
