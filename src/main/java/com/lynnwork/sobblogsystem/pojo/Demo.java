package com.lynnwork.sobblogsystem.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * tb_demo
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("tb_demo")
public class Demo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;


}
