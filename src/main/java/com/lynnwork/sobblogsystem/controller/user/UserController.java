package com.lynnwork.sobblogsystem.controller.user;


import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IUserService;
import com.lynnwork.sobblogsystem.utils.TextUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
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
    public static final int[] captcha_font_types = {
            Captcha.FONT_1, Captcha.FONT_2, Captcha.FONT_3,
            Captcha.FONT_4, Captcha.FONT_5,
    };

    @Autowired
    private IUserService userService;


    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, @RequestParam("captcha_key") String captchaKey) throws Exception {
        if (TextUtils.isEmpty(captchaKey) || captchaKey.length() < 13) {
            return;
        }
        long key = 0L;
        try {
            key = Long.parseLong(captchaKey);
        } catch (Exception e) {
            return;
        }

        response.setContentType("image/png");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        SpecCaptcha specCaptcha = new SpecCaptcha(200, 60, 5);
        specCaptcha.setFont(Captcha.FONT_1);
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        specCaptcha.out(response.getOutputStream());
    }

    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody User user, HttpServletRequest request) {
        return userService.initManagerAccount(user, request);
    }

    @PostMapping
    public ResponseResult register(@RequestBody User user) {
        //1.检查用户名是否已经注册
        //2.检查邮箱格式是否正确、是否注册、邮箱验证码是否正确
        //3.检查图灵验证码是否正确
        //(达到可以注册条件)4.补全数据(密码加密、注册IP、登录IP、角色...)
        //5.保存到数据库
        //6.返回结果
        return null;
    }
}

