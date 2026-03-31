package cn.net.susan.service.sms;

import cn.net.susan.enums.SmsTypeEnum;


public interface ISmsService {

    /**
     * 发送验证码
     *
     * @param phone       手机号
     * @param code        验证码
     * @param smsTypeEnum 短信类型
     * @return 是否发送成功
     */
    boolean sendCode(String phone, String code, SmsTypeEnum smsTypeEnum);
}
