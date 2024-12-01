package com.lynnwork.sobblogsystem.mapper;

import com.lynnwork.sobblogsystem.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * tb_user Mapper 接口
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
public interface UserMapper extends BaseMapper<User> {
    User selectByUserName(@Param("user_name") String userName);

    User selectByEmail(@Param("email") String email);
}
