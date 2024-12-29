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
 * tb_friend_link
 * </p>
 *
 * @author lynnkyle
 * @since 2024-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_friend_link")
public class FriendLink implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 友情链接名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 友情链接logo
     */
    private String logo;

    /**
     * 友情链接
     */
    private String url;

    /**
     * 顺序
     */
    @TableField("`order`")
    private Integer order;

    /**
     * 友情链接状态:0表示不可用, 1表示正常
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
