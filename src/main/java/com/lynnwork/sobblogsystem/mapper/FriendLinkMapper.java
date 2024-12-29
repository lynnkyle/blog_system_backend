package com.lynnwork.sobblogsystem.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lynnwork.sobblogsystem.pojo.FriendLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * tb_friend_link Mapper 接口
 * </p>
 *
 * @author lynnkyle
 * @since 2024-12-20
 */
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
    IPage<FriendLink> selectPageVo(IPage<FriendLink> page);

    int deleteFriendLink(@Param("id") String friendLinkId);
}
