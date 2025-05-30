package com.lynnwork.sobblogsystem.controller.user;


import com.lynnwork.sobblogsystem.mapper.UserMapper;
import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IUserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /*
       获取图灵验证码 /user/captcha
       @Param
       @Return
    */
    @GetMapping("/captcha")
    public void getCaptcha(@RequestParam("captcha_key") String captchaKey, HttpServletResponse resp) {
        try {
            userService.createCapture(captchaKey, resp);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /*
        发送邮件验证码 /user/email_code
        使用场景:注册、找回密码、修改邮箱（会输入新的邮箱）
        @Param
        1.邮箱地址
        2.使用场景的类型
        @Return
     */
    @GetMapping("/email_code")
    public ResponseResult sendEmailCode(@RequestParam("email") String emailAddress, @RequestParam("type") String type, HttpServletRequest req) {
        ResponseResult responseResult = null;
        try {
            responseResult = userService.sendEmailCode(emailAddress, type, req);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return responseResult;
    }

    /*
        检查邮箱是否已经注册
        @Param
        1.邮箱地址
        @Return
     */
    @ApiResponses({@ApiResponse(code = 20000, message = "当前邮箱已经注册"), @ApiResponse(code = 40000, message = "当前邮箱未注册")})
    @GetMapping("/email")
    public ResponseResult checkEmail(@RequestParam("email") String email) {
        return userService.checkEmail(email);
    }

    /*
        检查用户名是否已经注册
        @Param
        1.用户名
        @Return
     */
    @ApiResponses({@ApiResponse(code = 20000, message = "当前用户已经注册"), @ApiResponse(code = 40000, message = "当前用户未注册")})
    @GetMapping("/user_name")
    public ResponseResult checkUserName(@RequestParam("userName") String userName) {
        return userService.checkUserName(userName);
    }

    /*
        修改密码
        @Param
        1.邮箱验证码
        2.用户信息
        @Return
     */
    @PutMapping("/password/{email_code}")
    public ResponseResult updatePassword(@PathVariable("email_code") String emailCode, @RequestBody User user) {
        return userService.updatePassword(emailCode, user);
    }

    /*
        更新用户邮箱
        @Param
        1.邮箱验证码
        2.新邮箱地址
        @Return
     */
    @PutMapping("/email/{email_code}")
    public ResponseResult updateEmail(@PathVariable("email_code") String emailCode, @RequestParam("email") String email) {
        return userService.updateEmail(emailCode, email);
    }

    /*
        初始化管理员账号 /user/admin_account
        @Param
        1.用户信息
        @Return
     */
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody User user, HttpServletRequest req) {
        return userService.initManagerAccount(user, req);
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
    @PostMapping("/join_in")
    public ResponseResult register(@RequestBody User user, @RequestParam("email_code") String emailCode, @RequestParam("captcha_key") String captchaKey, @RequestParam("captcha_code") String captchaCode, HttpServletRequest req) {
        return userService.register(user, emailCode, captchaKey, captchaCode, req);
    }

    /*
        登录login /user/
        @Param
        1.图灵验证码的key
        2.图灵验证码
        3.用户账号-可以昵称,可以邮箱-->做了唯一性处理
        4.密码
        HttpServletResponse设置Cookie,将Token放入Cookie,并返回给浏览器
        @Return
     */
    @PostMapping("/login/{captcha_key}/{captcha}")
    public ResponseResult doLogin(@PathVariable("captcha_key") String captchaKey, @PathVariable("captcha") String captcha, @RequestBody User user, HttpServletRequest req, HttpServletResponse resp) {
        return userService.doLogin(captchaKey, captcha, user, req, resp);
    }

    /*
        获取用户信息
        @Param
        1.用户id
        @Return
     */
    @GetMapping("/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId) {
        return userService.getUserInfo(userId);
    }

    /*
        获取用户列表
        @Param
        1.分页页数
        2.分页大小
        @Return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listUsers(@PathVariable("page") int page, @PathVariable("size") int size) {
        return userService.listUsers(page, size);
    }

    /*
        更新用户信息
        @Param
        1.用户id
        2.用户信息
        @Return
     */
    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo(@PathVariable("userId") String userId, @RequestBody User user) {
        return userService.updateUserInfo(userId, user);
    }

    /*
        删除用户
        @Param
        1.用户id
        @Return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId") String userId) {
        return userService.deleteUser(userId);
    }

    /*
        退出登录
     */
    @GetMapping("/logout")
    public ResponseResult logout() {
        return userService.doLogout();
    }
}

