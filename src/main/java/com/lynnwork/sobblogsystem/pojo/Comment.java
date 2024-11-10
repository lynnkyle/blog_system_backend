package com.lynnwork.sobblogsystem.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * tb_comment
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 父内容
     */
    private String parentContent;

    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论用户的ID
     */
    private String userId;

    /**
     * 评论用户的头像
     */
    private String userAvatar;

    /**
     * 评论用户的名称
     */
    private String userName;

    /**
     * 状态(0:表示删除, 1:表示正常)
     */
    private String state;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
