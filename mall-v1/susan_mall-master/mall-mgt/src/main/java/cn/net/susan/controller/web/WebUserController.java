package cn.net.susan.controller.web;

import cn.net.susan.annotation.Limit;
import cn.net.susan.annotation.NoLogin;
import cn.net.susan.entity.auth.AuthUserEntity;
import cn.net.susan.entity.auth.CaptchaEntity;
import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.auth.TokenEntity;
import cn.net.susan.entity.sys.web.MyCountStatWebEntity;
import cn.net.susan.entity.sys.web.UserPhoneLoginWebEntity;
import cn.net.susan.entity.sys.web.UserWebEntity;
import cn.net.susan.enums.LimitTypeEnum;
import cn.net.susan.service.sys.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Api(tags = "web用户操作", description = "web用户接口")
@RestController
@RequestMapping("/v1/web/user")
@Validated
public class WebUserController {

    @Autowired
    private UserService userService;


    /**
     * 获取当前登录的用户详情
     *
     * @return 用户详情
     */
    @ApiOperation("获取当前登录的用户详情")
    @GetMapping(value = "/getUserDetail")
    public UserWebEntity getUserDetail() {
        return userService.getUserDetail();
    }

    /**
     * 我的数量统计
     *
     * @return 我的数量统计
     */
    @ApiOperation("我的数量统计")
    @GetMapping(value = "/myCountStat")
    public MyCountStatWebEntity myCountStat() {
        return userService.myCountStat();
    }


    /**
     * 用户手机号登录
     *
     * @param authUserEntity 用户实体
     * @return 影响行数
     */
    @NoLogin
    @Limit(key = "getCity", permitsPerSecond = 5, timeOut = 60, limitType = LimitTypeEnum.IP)
    @ApiOperation(notes = "用户登录", value = "用户登录")
    @PostMapping("/login")
    public TokenEntity login(@Valid @RequestBody AuthUserEntity authUserEntity) {
        return userService.login(authUserEntity);
    }

    /**
     * 用户手机号登录
     *
     * @param userPhoneLoginWebEntity 用户实体
     * @return 影响行数
     */
    @NoLogin
    @Limit(key = "getCity", permitsPerSecond = 5, timeOut = 60, limitType = LimitTypeEnum.IP)
    @ApiOperation(notes = "用户手机号登录", value = "用户手机号登录")
    @PostMapping("/loginByPhone")
    public TokenEntity loginByPhone(@Valid @RequestBody UserPhoneLoginWebEntity userPhoneLoginWebEntity) {
        return userService.loginByPhone(userPhoneLoginWebEntity);
    }

    /**
     * 用户退出登录
     *
     * @param request 请求
     * @return 影响行数
     */
    @NoLogin
    @ApiOperation(notes = "用户退出登录", value = "用户退出登录")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        userService.logout(request);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @NoLogin
    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public JwtUserEntity getUserInfo() {
        return userService.getUserInfo();
    }


    @NoLogin
    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public CaptchaEntity getCode() {
        return userService.getCode();
    }
}
