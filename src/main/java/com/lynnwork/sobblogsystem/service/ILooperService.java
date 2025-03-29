package com.lynnwork.sobblogsystem.service;

import com.lynnwork.sobblogsystem.pojo.Looper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynnwork.sobblogsystem.response.ResponseResult;

/**
 * <p>
 * tb_looper 服务类
 * </p>
 *
 * @author lynnkyle
 * @since 2025-01-24
 */
public interface ILooperService extends IService<Looper> {

    ResponseResult addLooper(Looper looper);

    ResponseResult deleteLooper(String looperId);

    ResponseResult updateLooper(String looperId, Looper looper);

    ResponseResult getLooper(String looperId);

    ResponseResult listLoopers(int page, int size);

}
