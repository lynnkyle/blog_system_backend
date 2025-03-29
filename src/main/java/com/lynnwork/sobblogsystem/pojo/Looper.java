package com.lynnwork.sobblogsystem.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * tb_looper
 * </p>
 *
 * @author lynnkyle
 * @since 2025-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_looper")
public class Looper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 轮播图标题
     */
    private String title;

    /**
     * 顺序
     */
    @TableField("`order`")
    private Integer order;

    /**
     * 状态(0:表示不可用,1:表示正常)
     */
    private String state;

    /**
     * 目标URL
     */
    private String targetUrl;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
