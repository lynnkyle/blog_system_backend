package com.lynnwork.sobblogsystem.service;

import com.lynnwork.sobblogsystem.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynnwork.sobblogsystem.response.ResponseResult;

/**
 * <p>
 * tb_article 服务类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
public interface IArticleService extends IService<Article> {

    ResponseResult postArticle(Article article);

    ResponseResult getArticle(String articleId);

    ResponseResult updateArticle(String articleId);

    ResponseResult deleteArticle(String articleId);

    ResponseResult listArticles(int page, int size, String keyword, String categoryId, String state);
}
