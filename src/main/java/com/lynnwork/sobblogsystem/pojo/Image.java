package com.lynnwork.sobblogsystem.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * tb_image
 * </p>
 *
 * @author lynnkyle
 * @since 2025-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_image")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 原名称
     */
    private String name;

    /**
     * 路径
     */
    private String url;

    /**
     * 存储路径
     */
    private String path;

    /**
     * 图片类型
     */
    private String contentType;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 状态(0:表示删除,1:表示正常)
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
