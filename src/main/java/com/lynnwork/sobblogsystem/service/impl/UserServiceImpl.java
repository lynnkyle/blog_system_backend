package com.lynnwork.sobblogsystem.service.impl;

import com.lynnwork.sobblogsystem.mapper.SettingMapper;
import com.lynnwork.sobblogsystem.pojo.Setting;
import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.mapper.UserMapper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.utils.Constants;
import com.lynnwork.sobblogsystem.utils.RedisUtil;
import com.lynnwork.sobblogsystem.utils.SnowflakeIdWorker;
import com.lynnwork.sobblogsystem.utils.TextUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

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
    public static final int[] captcha_font_types = {
            Captcha.FONT_1, Captcha.FONT_2, Captcha.FONT_3,
            Captcha.FONT_4, Captcha.FONT_5, Captcha.FONT_6,
            Captcha.FONT_7, Captcha.FONT_8, Captcha.FONT_9,
            Captcha.FONT_10
    };
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Random random;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SettingMapper settingMapper;

    /*
        初始化管理员账户
    */
    @Override
    public ResponseResult initManagerAccount(User user, HttpServletRequest request) {
        Setting setting = settingMapper.findOneByKey(Constants.Setting.ADMIN_ACCOUNT_INIT_STATE);
        if (setting != null) {
            return ResponseResult.FAILED("管理员账号已经初始化成功。");
        }
        if (TextUtil.isEmpty(user.getUserName())) {
            return ResponseResult.FAILED("用户名不能为空。");
        }
        if (TextUtil.isEmpty(user.getPassword())) {
            return ResponseResult.FAILED("密码不能为空。");
        }
        if (TextUtil.isEmpty(user.getEmail())) {
            return ResponseResult.FAILED("邮箱不能为空。");
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
            return ResponseResult.SUCCESS("管理员初始化成功。");
        } else {
            return ResponseResult.FAILED("管理员初始化失败。");
        }
    }

    /*
        生成图灵验证码
    */
    public void createCapture(HttpServletResponse response, String captchaKey) throws Exception {
        if (TextUtil.isEmpty(captchaKey) || captchaKey.length() < 13) {
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
        int captchaType = random.nextInt(3);
        Captcha captcha = null;
        if (captchaType == 0) {
            captcha = new SpecCaptcha(200, 60, 5);
        } else if (captchaType == 1) {
            captcha = new GifCaptcha(200, 60, 5);
        } else {
            captcha = new ArithmeticCaptcha(130, 48);
            captcha.setLen(3);  // 几位数运算，默认是两位
            captcha.text();  // 获取运算的结果：5
        }
        int fontType = random.nextInt(captcha_font_types.length);
        captcha.setFont(captcha_font_types[fontType]);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        String content = captcha.text().toLowerCase();
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey, content, 10 * 60);
        captcha.out(response.getOutputStream());
    }

    @Autowired
    private TaskService taskService;

    /*
        发送邮箱验证码
    */
    @Override
    public ResponseResult sendEmail(HttpServletRequest request, String emailAddress) throws Exception {
        /*
        1.防止暴力发送邮箱:
            1)同一邮箱间隔30s发送一次;
            2)同一Ip地址,最多只能发10次(如果是短信,最多只能发5次)
        */
        String emailIp = request.getRemoteAddr();
        emailIp = emailIp.replaceAll(":", "-");
        Integer ipSendTime = (Integer) redisUtil.get(Constants.User.KEY_EMAIL_SEND_IP + emailIp);
        if (ipSendTime != null && ipSendTime > 10) {
            return ResponseResult.FAILED("由于验证码请求过于频繁，为确保账户安全，请您于1小时后再尝试获取验证码。");
        }
        Object hasEmailSend = redisUtil.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        if (hasEmailSend != null) {
            return ResponseResult.FAILED("请您在30秒后再次尝试获取验证码。");
        }
        //2.检查邮箱地址是否正确
        boolean isEmailAddressOk = TextUtil.isEmailAddressOk(emailAddress);
        if (!isEmailAddressOk) {
            return ResponseResult.FAILED("邮箱格式不正确。");
        }
        //3.发送验证码，6位数100000~999999
        int verifyCode = random.nextInt(999999);
        if (verifyCode <= 100000) {
            verifyCode += 100000;
        }
        taskService.sendEmailVerifyCode(String.valueOf(verifyCode), emailAddress);
        //4.保存到缓存中
        if (ipSendTime == null) {
            ipSendTime = 0;
        }
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_IP + emailIp, ipSendTime + 1, 1 * 60 * 60);
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress, "email_send_true", 30);
        redisUtil.set(Constants.User.KEY_EMAIL_VERIFY_CODE_CONTENT + emailAddress, verifyCode, 10 * 60);
        return ResponseResult.SUCCESS("验证码发送成功。");
    }
}
