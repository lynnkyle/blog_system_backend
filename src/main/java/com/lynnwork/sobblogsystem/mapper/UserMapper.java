package com.lynnwork.sobblogsystem.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lynnwork.sobblogsystem.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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

    IPage<User> selectPageVo(IPage<User> page);

    int updatePasswordByEmail(@Param("password") String password, @Param("email") String email);

    int deleteUserByState(@Param("user_id") String userId);
}
