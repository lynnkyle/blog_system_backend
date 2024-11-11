package com.lynnwork.sobblogsystem.service.impl;

import com.lynnwork.sobblogsystem.mapper.SettingMapper;
import com.lynnwork.sobblogsystem.pojo.Setting;
import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.mapper.UserMapper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.utils.Constants;
import com.lynnwork.sobblogsystem.utils.SnowflakeIdWorker;
import com.lynnwork.sobblogsystem.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * tb_user 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SettingMapper settingMapper;

    @Override
    public ResponseResult initManagerAccount(User user, HttpServletRequest request) {
        Setting setting = settingMapper.findOneByKey(Constants.Setting.ADMIN_ACCOUNT_INIT_STATE);
        if (setting != null) {
            return ResponseResult.FAILED("管理员账号已经初始化成功");
        }
        // 1.检查数据
        if (TextUtils.isEmpty(user.getUserName())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }
        if (TextUtils.isEmpty(user.getEmail())) {
            return ResponseResult.FAILED("邮箱不能为空");
        }
        user.setId(String.valueOf(idWorker.nextId()));
        user.setUserName(user.getUserName());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Constants.User.ROLE_ADMIN);
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        user.setEmail(user.getEmail());
        user.setState(Constants.User.DEFAULT_STATE);
        user.setRegIp(request.getRemoteAddr());
        user.setLogIp(request.getRemoteAddr());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int check = userMapper.insert(user);
        setting = new Setting();
        setting.setId(String.valueOf(idWorker.nextId()));
        setting.setKey(Constants.Setting.ADMIN_ACCOUNT_INIT_STATE);
        setting.setValue("1");
        setting.setCreateTime(new Date());
        setting.setUpdateTime(new Date());
        settingMapper.insert(setting);
        if (check == 1) {
            return ResponseResult.SUCCESS("管理员初始化成功");
        } else {
            return ResponseResult.FAILED("管理员初始化失败");
        }
    }
}
