package com.lynnwork.sobblogsystem.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynnwork.sobblogsystem.pojo.FriendLink;
import com.lynnwork.sobblogsystem.mapper.FriendLinkMapper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IFriendLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.utils.Constants;
import com.lynnwork.sobblogsystem.utils.SnowflakeIdWorker;
import com.lynnwork.sobblogsystem.utils.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * tb_friend_link 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-12-20
 */
@Service
@Transactional
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements IFriendLinkService {
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public ResponseResult addFiendLink(FriendLink friendLink) {
        //1.检查数据
        if (TextUtil.isEmpty(friendLink.getName())) {
            return ResponseResult.FAILED("友情链接名称不可以为空。");
        }
        if (TextUtil.isEmpty(friendLink.getLogo())) {
            return ResponseResult.FAILED("友情链接Logo不可以为空。");
        }
        if (TextUtil.isEmpty(friendLink.getUrl())) {
            return ResponseResult.FAILED("友情链接Url不可以为空。");
        }
        //2.补全数据
        friendLink.setId(String.valueOf(idWorker.nextId()));
        friendLink.setState(Constants.FriendLink.DEFAULT_STATE);
        friendLink.setCreateTime(new Date());
        friendLink.setUpdateTime(new Date());
        //3.保存数据
        friendLinkMapper.insert(friendLink);
        //4.返回结果
        return ResponseResult.SUCCESS("添加友情链接成功");
    }

    @Override
    public ResponseResult deleteFriendLink(String friendLinkId) {
        int update = friendLinkMapper.deleteFriendLink(friendLinkId);
        if (update <= 0) {
            return ResponseResult.FAILED("友情链接不存在");
        }
        return ResponseResult.SUCCESS("友情链接删除成功");
    }

    @Override
    public ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink) {
        //1.查找数据
        FriendLink friendLinkFromDbById = friendLinkMapper.selectById(friendLinkId);
        if (friendLinkFromDbById == null) {
            return ResponseResult.FAILED("友情链接不存在。");
        }
        //2.更改数据(检查数据)
        friendLink.setId(friendLinkFromDbById.getId());
        String name = friendLink.getName();
        if (!TextUtil.isEmpty(name)) {
            friendLinkFromDbById.setName(name);
        }
        String logo = friendLink.getLogo();
        if (!TextUtil.isEmpty(logo)) {
            friendLinkFromDbById.setLogo(logo);
        }
        String url = friendLink.getUrl();
        if (!TextUtil.isEmpty(url)) {
            friendLinkFromDbById.setUrl(url);
        }
        friendLinkFromDbById.setOrder(friendLink.getOrder());
        String state = friendLink.getState();
        if (!TextUtil.isEmpty(state)) {
            friendLinkFromDbById.setState(state);
        }
        friendLinkFromDbById.setUpdateTime(new Date());
        //3.保存数据
        friendLinkMapper.updateById(friendLink);
        //4.返回结果
        return ResponseResult.SUCCESS("更新友情连接成功");
    }

    @Override
    public ResponseResult getFriendLink(String friendLinkId) {
        FriendLink friendLinkFromDbById = friendLinkMapper.selectById(friendLinkId);
        if (friendLinkFromDbById == null) {
            return ResponseResult.FAILED("友情链接不存在。");
        }
        return ResponseResult.SUCCESS("获取友情链接成功。").setData(friendLinkFromDbById);
    }

    @Override
    public ResponseResult listFriendLinks(int page, int size) {
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = Constants.Page.DEFAULT_PAGE;
        }
        if (size < Constants.Page.DEFAULT_SIZE) {
            size = Constants.Page.DEFAULT_SIZE;
        }
        IPage<FriendLink> iPage = new Page<>(page, size);
        IPage<FriendLink> iPageByDb = friendLinkMapper.selectPageVo(iPage);
        return ResponseResult.SUCCESS("获取友情链接列表成功。").setData(iPageByDb);
    }

}
