package com.lynnwork.sobblogsystem.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * tb_article
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    @TableField(exist = false)
    private User user;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 分类ID
     */
    private String categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型(0: 富文本, 1: markdown)
     */
    private String type;

    /**
     * 状态(0: 已发布, 1: 草稿, 2: 删除)
     */
    private String state;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 标签
     */
    private String labels;

    public void setLabels(String labels) {
        if (!labels.contains("-")) {
            this.label_list.add(labels);
        } else {
            String[] split = labels.split("-");
            List<String> strings = Arrays.asList(split);
            this.label_list.addAll(strings);
        }
        this.labels = labels;
    }

    @TableField(exist = false)
    private List<String> label_list = new ArrayList<>();

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
