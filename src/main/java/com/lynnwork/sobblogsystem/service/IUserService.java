package com.lynnwork.sobblogsystem.service;

import com.lynnwork.sobblogsystem.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynnwork.sobblogsystem.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * tb_user 服务类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
public interface IUserService extends IService<User> {

    void createCapture(HttpServletResponse response, String captchaKey) throws Exception;

    ResponseResult sendEmailCode(HttpServletRequest req, String emailAddress, String type) throws Exception;

    User checkUser(HttpServletRequest req, HttpServletResponse resp);

    ResponseResult checkEmail(String email);

    ResponseResult checkUserName(String userName);

    ResponseResult initManagerAccount(User user, HttpServletRequest req);

    ResponseResult register(User user, String emailCode, String captchaKey, String captchaCode, HttpServletRequest req);

    ResponseResult doLogin(String captchaKey, String captcha, User user, HttpServletRequest req, HttpServletResponse resp);

    ResponseResult getUserInfo(String userId);

    ResponseResult updateUserInfo(String userId, User user, HttpServletRequest req, HttpServletResponse resp);
}
