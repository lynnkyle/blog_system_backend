package com.lynnwork.sobblogsystem.service.impl;

import com.lynnwork.sobblogsystem.pojo.Article;
import com.lynnwork.sobblogsystem.mapper.ArticleMapper;
import com.lynnwork.sobblogsystem.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * tb_article 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
