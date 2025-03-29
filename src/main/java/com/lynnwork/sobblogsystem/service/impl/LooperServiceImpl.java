package com.lynnwork.sobblogsystem.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynnwork.sobblogsystem.pojo.Looper;
import com.lynnwork.sobblogsystem.mapper.LooperMapper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.ILooperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.utils.Constants;
import com.lynnwork.sobblogsystem.utils.SnowflakeIdWorker;
import com.lynnwork.sobblogsystem.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * <p>
 * tb_looper 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2025-01-24
 */
@Slf4j
@Service
public class LooperServiceImpl extends ServiceImpl<LooperMapper, Looper> implements ILooperService {

    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private LooperMapper looperMapper;


    @Override
    public ResponseResult addLooper(Looper looper) {
        //1.检查数据
        if (TextUtil.isEmpty(looper.getTitle())) {
            return ResponseResult.FAILED("轮播图标题不可以为空。");
        }
        if (TextUtil.isEmpty(looper.getTargetUrl())) {
            return ResponseResult.FAILED("轮播图跳转连接不可以为空。");
        }
        if (TextUtil.isEmpty(looper.getImageUrl())) {
            return ResponseResult.FAILED("轮播图图片不可以为空。");
        }
        //2.补充数据
        looper.setId(String.valueOf(idWorker.nextId()));
        looper.setState(Constants.Looper.DEFAULT_STATE);
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());
        //3.插入数据
        looperMapper.insert(looper);
        return ResponseResult.SUCCESS("添加轮播图成功。");
    }

    @Override
    public ResponseResult deleteLooper(String looperId) {
        looperMapper.deleteById(looperId);
        return ResponseResult.SUCCESS("删除轮播图成功。");
    }

    @Override
    public ResponseResult updateLooper(String looperId, Looper looper) {
        //1.查找数据
        Looper looperFromDbById = looperMapper.selectById(looperId);
        if (looperFromDbById == null) {
            return ResponseResult.FAILED("轮播图不存在。");
        }
        //2.更改数据(检查数据)
        String title = looper.getTitle();
        if (!TextUtil.isEmpty(title)) {
            looperFromDbById.setTitle(title);
        }
        looperFromDbById.setOrder(looper.getOrder());
        String state = looper.getState();
        if (!TextUtil.isEmpty(state)) {
            looperFromDbById.setState(state);
        }
        String targetUrl = looper.getTargetUrl();
        if (!TextUtil.isEmpty(targetUrl)) {
            looperFromDbById.setTargetUrl(targetUrl);
        }
        String imageUrl = looper.getImageUrl();
        if (!TextUtil.isEmpty(imageUrl)) {
            looperFromDbById.setImageUrl(imageUrl);
        }
        looperFromDbById.setUpdateTime(new Date());
        //3.保存数据
        looperMapper.updateById(looperFromDbById);
        //4.返回结果
        return ResponseResult.SUCCESS("更新轮播图成功。");
    }

    public ResponseResult getLooper(String looperId) {
        Looper looper = looperMapper.selectById(looperId);
        if (looper == null) {
            return ResponseResult.FAILED("轮播图不存在。");
        }
        return ResponseResult.SUCCESS("获取轮播图成功。").setData(looper);
    }

    @Override
    public ResponseResult listLoopers(int page, int size) {
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = Constants.Page.DEFAULT_PAGE;
        }
        if (size < Constants.Page.DEFAULT_SIZE) {
            size = Constants.Page.DEFAULT_SIZE;
        }
        IPage iPage = new Page(page, size);
        IPage<Looper> iPageByDb = looperMapper.selectPageVo(iPage);
        return ResponseResult.SUCCESS("获取轮播图列表成功。").setData(iPageByDb);
    }
}
