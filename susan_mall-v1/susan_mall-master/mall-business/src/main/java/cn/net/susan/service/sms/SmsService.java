package cn.net.susan.service.sms;

import cn.hutool.core.util.BooleanUtil;
import cn.net.susan.entity.common.CommonSmsRecordEntity;
import cn.net.susan.entity.common.web.CommonSmsRecordWebEntity;
import cn.net.susan.enums.SmsTypeEnum;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.helper.IdGenerateHelper;
import cn.net.susan.mapper.common.CommonSmsRecordMapper;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.util.RandomUtil;
import cn.net.susan.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

import static cn.net.susan.util.CaptchaKeyUtil.getCaptchaKey;
import static cn.net.susan.util.SmsKeyUtil.getSmsCodePrefixKey;


@Service
public class SmsService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISmsService iSmsService;
    @Value("${mall.mgt.registerSmsCodeExpireSecond:60}")
    private long registerSmsCodeExpireSecond;
    @Value("${mall.mgt.mockSms:false}")
    private Boolean mockSms;
    @Value("${mall.mgt.mockCode:123456}")
    private String mockCode;
    @Autowired
    private CommonSmsRecordMapper commonSmsRecordMapper;
    @Autowired
    private IdGenerateHelper idGenerateHelper;


    /**
     * 发送短信
     *
     * @param commonSmsRecordWebEntity 请求实体
     * @return 短信实体
     */
    public CommonSmsRecordWebEntity sendSmsCode(@RequestBody CommonSmsRecordWebEntity commonSmsRecordWebEntity) {
        try {
            SmsTypeEnum smsTypeEnum = getSmsTypeEnum(commonSmsRecordWebEntity.getType());
            AssertUtil.notNull(smsTypeEnum, "短信类型错误");

            String code = redisUtil.get(getCaptchaKey(commonSmsRecordWebEntity.getCaptchaUuid()));
            AssertUtil.hasLength(code, "该验证码已失效");
            AssertUtil.isTrue(code.trim().equals(commonSmsRecordWebEntity.getCaptchaCode().trim()), "验证码错误");

            boolean success = sendSmsCode(commonSmsRecordWebEntity.getPhone(), smsTypeEnum);
            if (!success) {
                throw new BusinessException("短信发送失败");
            }

            commonSmsRecordWebEntity.setExpireSecond(registerSmsCodeExpireSecond);
            return commonSmsRecordWebEntity;
        } finally {
            redisUtil.del(getCaptchaKey(commonSmsRecordWebEntity.getCaptchaUuid()));
        }
    }


    private SmsTypeEnum getSmsTypeEnum(Integer type) {
        for (SmsTypeEnum smsTypeEnum : SmsTypeEnum.values()) {
            if (smsTypeEnum.getValue().equals(type)) {
                return smsTypeEnum;
            }
        }
        return null;
    }


    /**
     * 发送注册短信
     *
     * @param phone 手机号
     * @return 是否成功
     */
    public boolean sendRegisterSms(String phone) {
        return sendSmsCode(phone, SmsTypeEnum.REGISTER);
    }

    private boolean sendSmsCode(String phone, SmsTypeEnum type) {
        String smsCodePrefixKey = getSmsCodePrefixKey(phone, type);
        String value = redisUtil.get(smsCodePrefixKey);
        if (StringUtils.hasLength(value)) {
            throw new BusinessException("当前请求太频繁了，请稍后重试");
        }

        boolean success;
        String code = "";

        if (BooleanUtil.isTrue(mockSms)) {
            code = mockCode;
            success = true;
        } else {
            String sixBitRandom = RandomUtil.getSixBitRandom();
            success = iSmsService.sendCode(phone, sixBitRandom, type);
        }
        if (success) {
            saveSmsRecord(phone, code);
            redisUtil.set(smsCodePrefixKey, code, registerSmsCodeExpireSecond);
            return true;
        }
        return false;
    }

    private void saveSmsRecord(String phone, String code) {
        CommonSmsRecordEntity commonSmsRecordEntity = new CommonSmsRecordEntity();
        commonSmsRecordEntity.setId(idGenerateHelper.nextId());
        commonSmsRecordEntity.setPhone(phone);
        commonSmsRecordEntity.setExpireSecond((int) registerSmsCodeExpireSecond);
        commonSmsRecordEntity.setSmsCode(code);
        commonSmsRecordEntity.setSendTime(new Date());
        FillUserUtil.fillCreateDefaultUserInfo(commonSmsRecordEntity);
        commonSmsRecordMapper.insert(commonSmsRecordEntity);
    }

}
