package com.lynnwork.sobblogsystem.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lynnwork.sobblogsystem.pojo.Looper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * tb_looper Mapper 接口
 * </p>
 *
 * @author lynnkyle
 * @since 2025-01-24
 */
public interface LooperMapper extends BaseMapper<Looper> {

    IPage<Looper> selectPageVo(IPage<Looper> page);

}
