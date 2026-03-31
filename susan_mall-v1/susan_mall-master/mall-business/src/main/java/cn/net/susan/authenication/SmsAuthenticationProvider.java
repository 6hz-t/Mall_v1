package cn.net.susan.authenication;

import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.sys.UserConditionEntity;
import cn.net.susan.entity.sys.UserEntity;
import cn.net.susan.enums.SmsTypeEnum;
import cn.net.susan.mapper.sys.UserMapper;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.util.RandomUtil;
import cn.net.susan.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static cn.net.susan.util.SmsKeyUtil.getSmsCodePrefixKey;


public class SmsAuthenticationProvider implements AuthenticationProvider {
    private static final String DEFAULT_NICK_NAME = "手机号注册用户";

    private final UserDetailsService userDetailsService;

    private final RedisUtil redisUtil;

    private final UserMapper userMapper;

    public SmsAuthenticationProvider(UserDetailsService userDetailsService, RedisUtil redisUtil, UserMapper userMapper) {
        this.userDetailsService = userDetailsService;
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取手机号
        String phone = (String) authentication.getPrincipal();
        // 获取验证码
        String captcha = (String) authentication.getCredentials();
        String smsCodePrefixKey = getSmsCodePrefixKey(phone, SmsTypeEnum.LOGIN);

        try {
            String smsCode = redisUtil.get(smsCodePrefixKey);
            AssertUtil.hasLength(smsCode, "该短信验证码已失效");
            AssertUtil.isTrue(smsCode.trim().equals(captcha), "短信验证码错误");

            UserConditionEntity userConditionEntity = new UserConditionEntity();
            userConditionEntity.setPhone(phone);
            List<UserEntity> userEntities = userMapper.searchByCondition(userConditionEntity);

            UserEntity userEntity;
            if (CollectionUtils.isEmpty(userEntities)) {
                userEntity = FillUserUtil.mockCurrentUser(() -> registerUser(phone));
            } else {
                userEntity = userEntities.get(0);
            }

            // 验证用户信息
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getUserName());
            if (userDetails == null) {
                throw new BadCredentialsException("未找到对应的用户,请先注册");
            }

            // 创建已认证的Token
            return new SmsAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } finally {
            redisUtil.del(smsCodePrefixKey);
        }

    }

    private UserEntity registerUser(String phone) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(getUsername());
        userEntity.setNickName(DEFAULT_NICK_NAME);
        userEntity.setPhone(phone);
        userMapper.insert(userEntity);
        return userEntity;
    }

    private String getUsername() {
        return RandomUtil.getSixBitRandom();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
