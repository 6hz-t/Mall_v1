package cn.net.susan.service.sms;

import cn.net.susan.config.AliyunSmsConfig;
import cn.net.susan.enums.SmsTypeEnum;
import cn.net.susan.exception.BusinessException;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
public class AliyunSmsService implements ISmsService {
    private static final String SEND_SMS_SUCCESS = "OK";

    @Autowired
    private AliyunSmsConfig aliyunSmsConfig;


    /**
     * 阿里云短信服务发送手机验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    public boolean send(String phone, String templateCode, String code) {
        if (!StringUtils.hasLength(phone)) {
            throw new BusinessException("phone不能为空");
        }

        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliyunSmsConfig.getAccessKeyId())
                .accessKeySecret(aliyunSmsConfig.getAccessKeySecret())
                .build());

        AsyncClient client = null;
        try {
            client = AsyncClient.builder()
                    .credentialsProvider(provider)
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride(aliyunSmsConfig.getHost())
                                    .setConnectTimeout(Duration.ofSeconds(30))
                    )
                    .build();

            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .phoneNumbers(phone)
                    .signName(aliyunSmsConfig.getSignName())
                    .templateCode(templateCode)
                    .templateParam(getParam(code))
                    .build();

            CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
            SendSmsResponse resp = response.get();
            log.info("发送阿里云短信返回值：{}", new Gson().toJson(resp));
            if (resp.getStatusCode() == HttpStatus.OK.value()) {
                SendSmsResponseBody body = resp.getBody();
                return SEND_SMS_SUCCESS.equals(body.getCode());
            }
        } catch (Exception e) {
            if (client != null) {
                client.close();
            }
        }

        return false;
    }

    @Override
    public boolean sendCode(String phone, String code, SmsTypeEnum smsTypeEnum) {
        return send(phone, getTemplateCode(smsTypeEnum), code);
    }

    private String getTemplateCode(SmsTypeEnum smsTypeEnum) {
        switch (smsTypeEnum) {
            case LOGIN:
                return aliyunSmsConfig.getLoginTemplateCode();
            case REGISTER:
                return aliyunSmsConfig.getRegisterTemplateCode();
        }
        throw new BusinessException("暂不支持其他短信类型");
    }

    private String getParam(String code) {
        return String.format("{\"code\":\"%s\"}", code);
    }
}
