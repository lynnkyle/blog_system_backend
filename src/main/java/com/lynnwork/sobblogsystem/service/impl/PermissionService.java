package com.lynnwork.sobblogsystem.service.impl;

import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.service.IUserService;
import com.lynnwork.sobblogsystem.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("permission")
public class PermissionService {

    @Autowired
    private IUserService userService;

    public boolean admin() {
        //1.检查用户是否登录
        User user = userService.checkUser();
        if (user == null) {
            return false;
        }
        //2.检查用户角色
        return Constants.User.ROLE_ADMIN.equals(user.getRole());
    }
}
