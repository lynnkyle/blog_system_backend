package com.lynnwork.sobblogsystem.controller;

import com.lynnwork.sobblogsystem.mapper.ArticleMapper;
import com.lynnwork.sobblogsystem.pojo.Article;
import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IUserService;
import com.lynnwork.sobblogsystem.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private IUserService userService;

    @PostMapping("/article")
    public ResponseResult Test(@RequestBody Article article, HttpServletRequest req, HttpServletResponse resp) {
        User user = userService.checkUser(req, resp);
        if (user == null) {
            return ResponseResult.FAILED("用户未登录。");
        }
        article.setId("1");
        article.setUserId(user.getId());
        article.setUserAvatar(user.getAvatar());
        article.setUserName(user.getUserName());
        article.setCategoryId(article.getCategoryId());
        article.setTitle(article.getTitle());
        article.setContent(article.getContent());
        article.setType(article.getType());
        article.setState(article.getState());
        article.setSummary(article.getSummary());
        article.setLabels(article.getLabels());
        article.setViewCount(article.getViewCount());
        article.setPublishTime(new Date());
        article.setUpdateTime(new Date());
        articleMapper.insert(article);
        return ResponseResult.SUCCESS("成功创建文章");
    }
}
