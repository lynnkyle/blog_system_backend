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
    ResponseResult initManagerAccount(User user, HttpServletRequest request);

    void createCapture(HttpServletResponse response, String captchaKey) throws Exception;

    ResponseResult sendEmailCode(HttpServletRequest request, String emailAddress, String type) throws Exception;

    ResponseResult register(User user, String emailCode, String captchaKey, String captchaCode, HttpServletRequest request);

    ResponseResult doLogin(String captchaKey, String captcha, User user, HttpServletRequest req, HttpServletResponse resp);

    User checkUser(HttpServletRequest req, HttpServletResponse resp);

}
