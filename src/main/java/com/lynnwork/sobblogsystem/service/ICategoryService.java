package com.lynnwork.sobblogsystem.service;

import com.lynnwork.sobblogsystem.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynnwork.sobblogsystem.response.ResponseResult;

/**
 * <p>
 * tb_category 服务类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
public interface ICategoryService extends IService<Category> {
    ResponseResult addCategory(Category category);

    ResponseResult getCategory(String categoryId);

    ResponseResult listCategories(int page, int size);

    ResponseResult updateCategory(String categoryId, Category category);

    ResponseResult deleteCategory(String categoryId);
}
