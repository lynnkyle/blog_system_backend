package com.lynnwork.sobblogsystem.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynnwork.sobblogsystem.mapper.UserMapper;
import com.lynnwork.sobblogsystem.pojo.Category;
import com.lynnwork.sobblogsystem.mapper.CategoryMapper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.utils.Constants;
import com.lynnwork.sobblogsystem.utils.SnowflakeIdWorker;
import com.lynnwork.sobblogsystem.utils.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private SnowflakeIdWorker idWorker;

    @Autowired
    private CategoryMapper categoryMapper;

    /*
        添加分类
     */
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
        category.setId(String.valueOf(idWorker.nextId()));
        category.setState(Constants.Category.DEFAULT_STATE);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        //3.保存数据
        categoryMapper.insert(category);
        //4.返回结果
        return ResponseResult.SUCCESS("成功插入分类。");
    }

    /*
        获取分类
     */
    @Override
    public ResponseResult getCategory(String categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            return ResponseResult.FAILED("分类不存在。");
        }
        return ResponseResult.SUCCESS("获取分类成功。").setData(category);
    }

    /*
        获取分类列表
     */
    @Override
    public ResponseResult listCategories(int page, int size) {
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = Constants.Page.DEFAULT_PAGE;
        }
        if (size < Constants.Page.DEFAULT_SIZE) {
            size = Constants.Page.DEFAULT_SIZE;
        }
        IPage<Category> iPage = new Page<>(page, size);
        IPage<Category> iPageByDb = categoryMapper.selectPageVo(iPage);
        return ResponseResult.SUCCESS("获取分类列表成功。").setData(iPageByDb);
    }

    @Override
    public ResponseResult updateCategory(String categoryId, Category category) {
        //1.查找数据
        Category categoryFromDbById = categoryMapper.selectById(categoryId);
        if (categoryFromDbById == null) {
            return ResponseResult.FAILED("分类不存在。");
        }
        //2.更改数据(检查数据)
        String name = category.getName();
        if (!TextUtil.isEmpty(name)) {
            categoryFromDbById.setName(name);
        }
        String pinyin = category.getPinyin();
        if (!TextUtil.isEmpty(pinyin)) {
            categoryFromDbById.setPinyin(pinyin);
        }
        String description = category.getDescription();
        if (!TextUtil.isEmpty(description)) {
            categoryFromDbById.setDescription(description);
        }
        categoryFromDbById.setOrder(category.getOrder());
        String state = category.getState();
        if (!TextUtil.isEmpty(state)) {
            categoryFromDbById.setState(state);
        }
        categoryFromDbById.setUpdateTime(new Date());
        //3.保存数据
        categoryMapper.updateById(categoryFromDbById);
        //4.返回结果
        return ResponseResult.SUCCESS("分类更新成功。");
    }

    @Override
    public ResponseResult deleteCategory(String categoryId) {
        int res = categoryMapper.deleteCategoryByState(categoryId);
        return res > 0 ? ResponseResult.SUCCESS("删除分类成功") : ResponseResult.SUCCESS("分类不存在");
    }
}
