package com.lynnwork.sobblogsystem.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * refresh_token表
 * </p>
 *
 * @author lynnkyle
 * @since 2024-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_refresh_token")
public class RefreshToken implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * Refresh Token
     */
    private String refreshToken;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * TOKEN键值
     */
    private String tokenKey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
