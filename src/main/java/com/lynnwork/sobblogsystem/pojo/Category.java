package com.lynnwork.sobblogsystem.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * tb_category
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类拼音
     */
    private String pinyin;

    /**
     * 描述
     */
    private String description;

    /**
     * 顺序
     */
    @TableField("`order`")
    private Integer order;

    /**
     * 状态: 0表示不使用, 1表示正常
     */
    private String state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
