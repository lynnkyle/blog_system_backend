package com.lynnwork.sobblogsystem.service;

import com.lynnwork.sobblogsystem.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lynnwork.sobblogsystem.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

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

    ResponseResult sendEmail(HttpServletRequest request, String emailAddress) throws Exception;
}
