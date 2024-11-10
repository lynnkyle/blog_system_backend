package com.lynnwork.sobblogsystem.mapper;

import com.lynnwork.sobblogsystem.pojo.Setting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * tb_setting Mapper 接口
 * </p>
 *
 * @author lynnkyle
 * @since 2024-11-04
 */
public interface SettingMapper extends BaseMapper<Setting> {
    Setting findOneByKey(String key);
}
