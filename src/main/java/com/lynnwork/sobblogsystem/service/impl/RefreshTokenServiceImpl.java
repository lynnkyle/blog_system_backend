package com.lynnwork.sobblogsystem.service.impl;

import com.lynnwork.sobblogsystem.pojo.RefreshToken;
import com.lynnwork.sobblogsystem.mapper.RefreshTokenMapper;
import com.lynnwork.sobblogsystem.service.IRefreshTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * refresh_token表 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-12-01
 */
@Service
public class RefreshTokenServiceImpl extends ServiceImpl<RefreshTokenMapper, RefreshToken> implements IRefreshTokenService {

}
