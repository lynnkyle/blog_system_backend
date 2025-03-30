package com.lynnwork.sobblogsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynnwork.sobblogsystem.mapper.ArticleNoContentMapper;
import com.lynnwork.sobblogsystem.mapper.UserMapper;
import com.lynnwork.sobblogsystem.pojo.Article;
import com.lynnwork.sobblogsystem.mapper.ArticleMapper;
import com.lynnwork.sobblogsystem.pojo.ArticleNoContent;
import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.service.IUserService;
import com.lynnwork.sobblogsystem.utils.Constants;
import com.lynnwork.sobblogsystem.utils.SnowflakeIdWorker;
import com.lynnwork.sobblogsystem.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * tb_article 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Slf4j
@Service
@Transactional
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private SnowflakeIdWorker idWorker;

    @Autowired
    private IUserService userService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleNoContentMapper articleNoContentMapper;

    @Override
    public ResponseResult postArticle(Article article) {
        User user = userService.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        // 1. 检查数据
        String title = article.getTitle();
        if (TextUtil.isEmpty(title)) {
            return ResponseResult.FAILED("文章标题不可以为空。");
        }
        if (title.length() > Constants.Article.TITLE_MAX_LENGTH) {
            return ResponseResult.FAILED("文章标题不可以超过" + Constants.Article.TITLE_MAX_LENGTH + "个字符。");
        }
        String type = article.getType();
        if (TextUtil.isEmpty(type)) {
            return ResponseResult.FAILED("文章类型不可以为空。");
        }
        if (!Constants.Article.TITLE_TYPE_TEXT.equals(type) && !Constants.Article.TITLE_TYPE_MARKDOWN.equals(type)) {
            return ResponseResult.FAILED("文章类型格式不正确。");
        }
        String state = article.getState();
        if (!Constants.Article.STATE_PUBLISH.equals(state) && !Constants.Article.STATE_DRAFT.equals(state)) {
            return ResponseResult.FAILED("不支持此操作。");
        }
        //  发布检查
        if (Constants.Article.STATE_PUBLISH.equals(state)) {
            if (TextUtil.isEmpty(article.getCategoryId())) {
                return ResponseResult.FAILED("文章分类不可以为空。");
            }
            if (TextUtil.isEmpty(article.getContent())) {
                return ResponseResult.FAILED("文章内容不可以为空。");
            }
            String summary = article.getSummary();
            if (TextUtil.isEmpty(summary)) {
                return ResponseResult.FAILED("文章摘要不可以为空。");
            }
            if (summary.length() > Constants.Article.SUMMARY_MAX_LENGTH) {
                return ResponseResult.FAILED("文章摘要不可以超过" + Constants.Article.SUMMARY_MAX_LENGTH + "个字符。");
            }
            if (TextUtil.isEmpty(article.getLabels())) {
                return ResponseResult.FAILED("文章标签不可以为空。");
            }
        }
        // 2. 补全数据
        String articleId = article.getId();
        if (TextUtil.isEmpty(articleId)) {
            //  新增内容
            article.setId(String.valueOf(idWorker.nextId()));
            article.setPublishTime(new Date());
        } else {
            //  更新内容
            Article articleFromDb = articleMapper.selectById(articleId);
            if (Constants.Article.STATE_PUBLISH.equals(articleFromDb.getState()) || Constants.Article.STATE_DRAFT.equals(articleFromDb.getState())) {
                // 已经发布了，只能更新，不能保存为草稿
                return ResponseResult.FAILED("已发布文章了不支持保存草稿。");
            }
        }
        article.setUserId(user.getId());
        article.setUserAvatar(user.getAvatar());
        article.setUserName(user.getUserName());
        article.setUpdateTime(new Date());
        // 3. 保存数据
        articleMapper.insert(article);
        // 4. 返回结果
        // 返回结果，只有一种case使用到该id
        // 程序自动保存成草稿(每30s保存一次, 需要加上id, 否则会创建多个item)
        return ResponseResult.SUCCESS(Constants.Article.STATE_PUBLISH.equals(state) ? "文章发表成功。" : "草稿保存成功。").setData(article.getId());
    }

    @Override
    public ResponseResult updateArticle(String articleId) {
        return null;
    }

    /**
     * 获取文章详情
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult getArticle(String articleId) {
        // 1.查询文章
        Article article = articleMapper.getArticleWithUserProfile(articleId);
        User user_ = article.getUser();
        log.info("user===>", user_);
        if (article == null) {
            return ResponseResult.FAILED("文章不存在。");
        }
        // 2.判断文章状态
        String state = article.getState();
        if (Constants.Article.STATE_PUBLISH.equals(state) || Constants.Article.STATE_TOP.equals(state)) {
            return ResponseResult.SUCCESS("获取文章成功。").setData(article);
        }
        User user = userService.checkUser();
        String role = user.getRole();
        if (!Constants.User.ROLE_ADMIN.equals(role)) {
            return ResponseResult.PERMISSION_DENY();
        }
        return ResponseResult.SUCCESS("获取文章成功。").setData(article);
    }

    @Override
    public ResponseResult deleteArticle(String articleId) {

        return null;
    }

    /**
     * 获取文章列表
     * @param page 页码
     * @param size 页面大小
     * @param keyword 标题关键字(搜索关键字)
     * @param categoryId 分类id
     * @param state 状态: 0.发布 1.草稿 2.删除 3.置顶
     * @return
     */
    @Override
    public ResponseResult listArticles(int page, int size, String keyword, String categoryId, String state) {
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = Constants.Page.DEFAULT_PAGE;
        }
        if (size < Constants.Page.DEFAULT_SIZE) {
            size = Constants.Page.DEFAULT_SIZE;
        }
        QueryWrapper<ArticleNoContent> wrapper = new QueryWrapper<>();
        if (!TextUtil.isEmpty(keyword)) {
            wrapper.like("title", keyword);
        }
        if (!TextUtil.isEmpty(categoryId)) {
            wrapper.eq("category_id", categoryId);
        }
        if (!TextUtil.isEmpty(state)) {
            wrapper.eq("state", state);
        }
        IPage<ArticleNoContent> iPage = new Page<>(page, size);
        IPage<ArticleNoContent> iPageById = articleNoContentMapper.selectPage(iPage, wrapper);
        return ResponseResult.SUCCESS("获取文章列表成功。").setData(iPageById);
    }
}
