package com.lynnwork.sobblogsystem.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lynnwork.sobblogsystem.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * tb_article Mapper 接口
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
public interface ArticleMapper extends BaseMapper<Article> {
    Article getArticleWithUserProfile(String articleId);
}
