package com.lynnwork.sobblogsystem.service.impl;

import com.google.gson.Gson;
import com.lynnwork.sobblogsystem.mapper.RefreshTokenMapper;
import com.lynnwork.sobblogsystem.mapper.SettingMapper;
import com.lynnwork.sobblogsystem.pojo.RefreshToken;
import com.lynnwork.sobblogsystem.pojo.Setting;
import com.lynnwork.sobblogsystem.pojo.User;
import com.lynnwork.sobblogsystem.mapper.UserMapper;
import com.lynnwork.sobblogsystem.response.ResponseResult;
import com.lynnwork.sobblogsystem.response.ResponseState;
import com.lynnwork.sobblogsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lynnwork.sobblogsystem.utils.*;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * tb_user 服务实现类
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@Slf4j
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
    private Random random;
    @Autowired
    private Gson gson;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SettingMapper settingMapper;
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;

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
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, 10 * 60);
        captcha.out(response.getOutputStream());
    }

    /*
        发送邮箱验证码
        使用场景: 注册、找回密码、修改邮箱(输入新的邮箱)
        注册(register):如果已经注册,提示该邮箱已经注册
        找回密码(forget):如果没有注册过,提示该邮箱没有注册
        修改邮箱(update):如果新的邮箱地址已经注册,提示该邮箱已注册;
     */
    @Override
    public ResponseResult sendEmailCode(HttpServletRequest req, String emailAddress, String type) throws Exception {
        if (emailAddress == null) {
            ResponseResult.FAILED("邮箱地址不可以为空。");
        }
        if ("register".equals(type) || "update".equals(type)) {
            User userByEmail = userMapper.selectByEmail(emailAddress);
            if (userByEmail != null) {
                return ResponseResult.FAILED("该邮箱已注册。");
            }
        } else if ("forget".equals(type)) {
            User userByEmail = userMapper.selectByEmail(emailAddress);
            if (userByEmail != null) {
                return ResponseResult.FAILED("该邮箱未注册。");
            }
        }
        /*
        1.防止暴力发送邮箱:
            1)同一邮箱间隔30s发送一次;
            2)同一Ip地址,1h最多只能发10次(如果是短信,最多只能发5次)
        */
        String emailIp = req.getRemoteAddr();
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
        redisUtil.set(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddress, String.valueOf(verifyCode), 10 * 60);
        return ResponseResult.SUCCESS("验证码发送成功。");
    }

    /*
        用户检查 Double Token的方案
        系统的增删改功能均需要涉及用户检查
     */
    @Override
    public User checkUser(HttpServletRequest req, HttpServletResponse resp) {
        /*
            Double Token方案:
            1.token:有效期2小时，存放在redis中
            2.refreshToken:有效期30天，存放在mysql中
        */
        //1.从Cookie中拿到tokenKey
        String tokenKey = CookieUtils.getCookieValue(req, Constants.User.KEY_COOKIE_TOKEN);
        if (TextUtil.isEmpty(tokenKey)) { //tokenKey无内容 当前访问未登录
            return null;
        }
        //2.解析tokenKey
        User userFromToken = parseByTokenKey(tokenKey);
        //2.1 redis中的token过期了(查询refreshToken)
        if (userFromToken == null) {
            //2.1.1 mysql中查询refreshToken
            RefreshToken refreshTokenFromDbByTokenKey = refreshTokenMapper.selectByTokenKey(tokenKey);
            if (refreshTokenFromDbByTokenKey == null) { // refreshTokenFromDbByTokenKey==null，当前访问未登录
                return null;
            }
            log.info("refreshTokenFromDbByTokenKey:{}", refreshTokenFromDbByTokenKey);
            //2.1.2 mysql中的refreshToken是否过期
            try {
                JwtUtil.parseToken(refreshTokenFromDbByTokenKey.getRefreshToken());
                // refreshToken未过期，重新生成token和refreshToken
                String userId = refreshTokenFromDbByTokenKey.getUserId();
                User userFromDbById = userMapper.selectById(userId);
                String newTokenKey = createToken(userFromDbById, resp);
                return parseByTokenKey(newTokenKey);
            } catch (Exception e) {
                // refreshToken过期，用户需要重新登录
                return null;
            }
        }
        //2.2 redis中的token未过期
        return userFromToken;
    }

    /*
        检查邮箱
     */
    @Override
    public ResponseResult checkEmail(String email) {
        User userFromDbByEmail = userMapper.selectByEmail(email);
        return userFromDbByEmail == null ? ResponseResult.FAILED("该邮箱未注册。") : ResponseResult.SUCCESS("该邮箱已注册。");
    }

    /*
        检查用户名
     */
    @Override
    public ResponseResult checkUserName(String userName) {
        User userFromDbByUserName = userMapper.selectByUserName(userName);
        return userFromDbByUserName == null ? ResponseResult.FAILED("该用户名未注册。") : ResponseResult.SUCCESS("该用户名已注册。");
    }

    /*
        初始化管理员账户
     */
    @Override
    public ResponseResult initManagerAccount(User user, HttpServletRequest req) {
        //1.检查是否有初始化(setting)
        Setting settingFromDbByKey = settingMapper.selectByKey(Constants.Setting.ADMIN_ACCOUNT_INIT_STATE);
        if (settingFromDbByKey != null) {
            return ResponseResult.FAILED("管理员账号已经初始化成功。");
        }
        //2.检查数据(username、password、email)
        if (TextUtil.isEmpty(user.getUserName())) {
            return ResponseResult.FAILED("用户名不能为空。");
        }
        if (TextUtil.isEmpty(user.getPassword())) {
            return ResponseResult.FAILED("密码不能为空。");
        }
        if (TextUtil.isEmpty(user.getEmail())) {
            return ResponseResult.FAILED("邮箱不能为空。");
        }
        //3.补充用户数据
        user.setId(String.valueOf(idWorker.nextId()));
        user.setUserName(user.getUserName());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Constants.User.ROLE_ADMIN);
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        user.setEmail(user.getEmail());
        user.setState(Constants.User.DEFAULT_STATE);
        user.setRegIp(req.getRemoteAddr());
        user.setLogIp(req.getRemoteAddr());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        //4.保存到数据库中
        userMapper.insert(user);
        //5.更新已经添加的标记(setting)
        Setting setting = new Setting();
        setting.setId(String.valueOf(idWorker.nextId()));
        setting.setKey(Constants.Setting.ADMIN_ACCOUNT_INIT_STATE);
        setting.setValue("1");
        setting.setCreateTime(new Date());
        setting.setUpdateTime(new Date());
        settingMapper.insert(setting);
        return ResponseResult.SUCCESS("管理员初始化成功。");
    }

    @Autowired
    private TaskService taskService;


    /*
        用户注册
     */
    @Override
    public ResponseResult register(User user, String emailCode, String captchaKey, String captchaCode, HttpServletRequest req) {
        //1.检查用户名是否已经注册
        String userName = user.getUserName();
        if (TextUtil.isEmpty(userName)) {
            return ResponseResult.FAILED("用户名不可为空。");
        }
        User userFromDbByUserName = userMapper.selectByUserName(userName);
        if (userFromDbByUserName != null) {
            return ResponseResult.FAILED("该用户名已注册。");
        }
        //2.检查邮箱格式是否正确、是否注册、邮箱验证码是否正确
        String email = user.getEmail();
        if (!TextUtil.isEmailAddressOk(email)) {
            return ResponseResult.FAILED("邮箱格式不正确。");
        }
        User userFromDbByEmail = userMapper.selectByEmail(email);
        if (userFromDbByEmail != null) {
            return ResponseResult.FAILED("该邮箱已经注册。");
        }
        String emailVerifyCode = (String) redisUtil.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (TextUtil.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("邮箱验证码无效。");
        }
        if (!emailVerifyCode.equals(emailCode.toLowerCase())) {
            return ResponseResult.FAILED("邮箱验证码不正确。");
        } else {
            redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        }
        //3.检查图灵验证码是否正确
        String captchaVerifyCode = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (TextUtil.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILED("人类验证码已过期。");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILED("人类验证码错误。");
        } else {
            redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        }
        //(达到可以注册条件)4.补全数据(密码加密、注册IP、登录IP、角色...)
        String password = user.getPassword();
        if (TextUtil.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空。");
        }
        user.setPassword(encoder.encode(password));
        user.setRole(Constants.User.ROLE_ADMIN);
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        user.setState(Constants.User.DEFAULT_STATE);
        user.setRegIp(req.getRemoteAddr());
        user.setLogIp(req.getRemoteAddr());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        //5.保存到数据库
        userMapper.insert(user);
        //6.返回结果
        return ResponseResult.GET(ResponseState.JOIN_IN_SUCCESS);
    }

    /*
        用户登录
     */
    @Override
    public ResponseResult doLogin(String captchaKey, String captcha, User user, HttpServletRequest req, HttpServletResponse resp) {
        //1.检查图灵验证码是否正确
        String captchaValue = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (!captchaValue.equals(captcha.toLowerCase())) {
            return ResponseResult.FAILED("人类验证码不正确。");
        }
        //2.根据用户名或密码查找用户
        String userName = user.getUserName();
        if (TextUtil.isEmpty(userName)) {
            return ResponseResult.FAILED("账号不可以为空");
        }
        String password = user.getPassword();
        if (TextUtil.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空");
        }
        User userFromDbByNameOrEmail = userMapper.selectByUserName(userName);
        if (userFromDbByNameOrEmail == null) {
            userFromDbByNameOrEmail = userMapper.selectByEmail(user.getEmail());
        }
        if (userFromDbByNameOrEmail == null) {
            return ResponseResult.FAILED("用户名或邮箱不正确。");
        }
        //3.验证密码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(password);
        if (!bCryptPasswordEncoder.matches(user.getPassword(), encodePassword)) {
            return ResponseResult.FAILED("密码不正确。");
        }

        if (!("1".equals(userFromDbByNameOrEmail.getState()))) {
            return ResponseResult.FAILED("当前账号已被冻结。");
        }
        //4.生成Token
        createToken(userFromDbByNameOrEmail, resp);
        return ResponseResult.SUCCESS("登录成功");
    }

    /*
       创建token和refreshToken(用户登录附属流程)
     */
    private String createToken(User userFromDbByNameOrEmail, HttpServletResponse resp) {
        refreshTokenMapper.deleteByUserId(userFromDbByNameOrEmail.getId());
        Map<String, Object> claims = ClaimsUtils.user2Claims(userFromDbByNameOrEmail);
        String token = JwtUtil.createToken(claims);
        //4.1 返回tokenKey,即token的md5值,返回给前端并将(tokenKey, token)存储到redis中
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        redisUtil.set(Constants.User.KEY_TOKEN_CONTENT + tokenKey, token, Constants.TimeValueInSecond.HOUR_2);
        CookieUtils.setUpCookie(resp, Constants.User.KEY_COOKIE_TOKEN, tokenKey, Constants.TimeValueInSecond.HOUR_2);
        String refreshTokenValue = JwtUtil.createRefreshToken(userFromDbByNameOrEmail.getId(), Constants.TimeValueInSecond.MONTH);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(String.valueOf(idWorker.nextId()));
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(userFromDbByNameOrEmail.getId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenMapper.insert(refreshToken);
        return tokenKey;
    }

    /*
       解析tokenKey(用户登录附属流程)
     */
    private User parseByTokenKey(String tokenKey) {
        String token = (String) redisUtil.get(Constants.User.KEY_TOKEN_CONTENT + tokenKey);
        if (token != null) {
            try {
                Claims claims = JwtUtil.parseToken(token);
                return ClaimsUtils.claims2User(claims);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /*
        获取用户信息
     */
    @Override
    public ResponseResult getUserInfo(String userId) {
        // 1.数据库中查找用户
        User userFromDbById = userMapper.selectById(userId);
        if (userFromDbById == null) {
            return ResponseResult.FAILED("用户不存在。");
        }
        // 2.复制对象，清空敏感性数据
        String userToJson = gson.toJson(userFromDbById);
        User jsonToUser = gson.fromJson(userToJson, User.class);
        jsonToUser.setPassword(null);
        jsonToUser.setEmail(null);
        jsonToUser.setRegIp(null);
        jsonToUser.setLogIp(null);
        return ResponseResult.SUCCESS("成功获取用户信息").setData(jsonToUser);
    }

    /*
        更新用户信息
        允许用户修改的信息   (修改密码、修改邮箱均需要验证)
        1.用户名   (唯一性)
        2.密码    (唯一性) (单独修改)
        3.头像
        4.邮箱    (单独修改)
        5.签名
     */

    @Override
    public ResponseResult updateUserInfo(String userId, User user, HttpServletRequest req, HttpServletResponse resp) {
        User userFromToken = checkUser(req, resp);
        if (userFromToken == null) {
            return ResponseResult.GET(ResponseState.ACCOUNT_NOT_LOGIN);
        }
        if (!userFromToken.getId().equals(userId)) {
            return ResponseResult.FAILED("无权限修改。");
        }
        //  设置用户名
        String userName = user.getUserName();
        if (!TextUtil.isEmpty(userName)) {
            User userFromDbByUserName = userMapper.selectByUserName(userName);
            if (userFromDbByUserName != null) {
                return ResponseResult.FAILED("该用户名已注册");
            }
            userFromToken.setUserName(userName);
        }
        //  设置头像
        if (!TextUtil.isEmpty(user.getAvatar())) {
            userFromToken.setAvatar(user.getAvatar());
        }
        //  设置签名
        userFromToken.setSign(user.getSign());

    }
}
