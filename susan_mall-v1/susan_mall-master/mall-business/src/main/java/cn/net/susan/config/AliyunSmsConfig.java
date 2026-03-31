package cn.net.susan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Data
public class AliyunSmsConfig {

    private String host;

    private String signName;

    private String accessKeyId;

    private String accessKeySecret;

    private String registerTemplateCode;

    private String loginTemplateCode;
}
