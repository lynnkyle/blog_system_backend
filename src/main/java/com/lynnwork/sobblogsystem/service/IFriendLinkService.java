package com.lynnwork.sobblogsystem.service;

import com.lynnwork.sobblogsystem.pojo.FriendLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * tb_friend_link 服务类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-12-20
 */
public interface IFriendLinkService extends IService<FriendLink> {

    ResponseResult addFiendLink(FriendLink friendLink);

    ResponseResult getFriendLink(String friendLinkId);

    ResponseResult listFriendLinks(int page, int size);

    ResponseResult deleteFriendLink(String friendLinkId);

    ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink);
}
