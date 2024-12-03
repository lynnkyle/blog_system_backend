package com.lynnwork.sobblogsystem.mapper;

import com.lynnwork.sobblogsystem.pojo.RefreshToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * refresh_token表 Mapper 接口
 * </p>
 *
 * @author lynnkyle
 * @since 2024-12-01
 */
public interface RefreshTokenMapper extends BaseMapper<RefreshToken> {
    RefreshToken selectByTokenKey(@Param("token_key") String tokenKey);

    int deleteByUserId(@Param("user_id") String userId);
}
