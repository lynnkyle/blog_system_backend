package com.lynnwork.sobblogsystem.service.impl;

import com.lynnwork.sobblogsystem.pojo.Category;
import com.lynnwork.sobblogsystem.mapper.CategoryMapper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.utils.Constants;
import com.lynnwork.sobblogsystem.utils.TextUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * tb_category 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public ResponseResult addCategory(Category category) {
        //1.检查数据(分类名称、分类pinyin、顺序、描述)
        if (TextUtil.isEmpty(category.getName())) {
            return ResponseResult.FAILED("分类名称不可以为空。");
        }
        if (TextUtil.isEmpty(category.getPinyin())) {
            return ResponseResult.FAILED("分类拼音不可以为空。");
        }
        if (TextUtil.isEmpty(String.valueOf(category.getOrder()))) {
            return ResponseResult.FAILED("分类顺序不可以为空。");
        }
        if (TextUtil.isEmpty(category.getDescription())) {
            return ResponseResult.FAILED("分类描述不可以为空。");
        }
        //2.补全数据
        category.setState(Constants.Category.DEFAULT_STATE);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        //3.保存数据
        categoryMapper.insert(category);
        //4.返回结果
        return ResponseResult.SUCCESS("成功插入分类");
    }
}
