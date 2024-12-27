package com.lynnwork.sobblogsystem.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lynnwork.sobblogsystem.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * tb_category Mapper 接口
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
public interface CategoryMapper extends BaseMapper<Category> {
    IPage<Category> selectPageVo(IPage<Category> page);

    int deleteCategoryByState(@Param("id") String categoryId);
}
