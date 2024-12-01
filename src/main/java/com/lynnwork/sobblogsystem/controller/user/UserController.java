package com.lynnwork.sobblogsystem.controller.user;


import com.lynnwork.sobblogsystem.mapper.UserMapper;
import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * tb_user 前端控制器
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private UserMapper userMapper;

    /*
       获取图灵验证码 /user/captcha
       @Param
       @Return
    */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, @RequestParam("captcha_key") String captchaKey) {
        try {
            userService.createCapture(response, captchaKey);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /*
        发送邮件验证码 /user/email_code
        使用场景：注册、找回密码、修改邮箱（会输入新的邮箱）
        @Param
        1.邮箱地址
        2.使用场景的类型
        @Return
     */
    @GetMapping("/email_code")
    public ResponseResult sendEmailCode(HttpServletRequest request, @RequestParam("email") String emailAddress,
                                        @RequestParam("type") String type) {
        ResponseResult responseResult = null;
        try {
            responseResult = userService.sendEmailCode(request, emailAddress, type);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return responseResult;
    }

    /*
        初始化管理员账号 /user/admin_account
        @Param
        1.用户信息()
        @Return
     */
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody User user, HttpServletRequest request) {
        return userService.initManagerAccount(user, request);
    }

    /*
        注册 /user?
        @Param
        1.用户信息(账号、密码、邮箱)
        2.邮箱验证码
        3.图灵验证码的key
        4.图灵验证码
        @Return
     */
    @PostMapping
    public ResponseResult register(@RequestBody User user,
                                   @RequestParam("email_code") String emailCode,
                                   @RequestParam("captcha_key") String captchaKey,
                                   @RequestParam("captcha_code") String captchaCode,
                                   HttpServletRequest request) {
        return userService.register(user, emailCode, captchaKey, captchaCode, request);
    }

    /*
        登录login /user/
        @Param
        1.用户账号-可以昵称,可以邮箱-->做了唯一性处理
        2.密码
        3.图灵验证码的key
        4.图灵验证码
        HttpServletResponse设置Cookie,将Token放入Cookie,并返回给浏览器
        @Return
     */
    @PostMapping("/{captcha_key}/{captcha}")
    public ResponseResult doLogin(@PathVariable("captcha_key") String captchaKey, @PathVariable("captcha") String captcha, @RequestBody User user, HttpServletRequest req, HttpServletResponse resp) {
        return userService.doLogin(captchaKey, captcha, user, req, resp);
    }
}

