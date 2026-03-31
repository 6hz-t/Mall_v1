package cn.net.susan.config;

import cn.net.susan.config.properties.AliPayProperties;
import com.alipay.easysdk.kernel.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AliPayConfig {

    @Autowired
    private BusinessConfig businessConfig;

    @Bean
    public Config config() {
        AliPayProperties aliPayConfig = businessConfig.getAliPayConfig();
        Config config = new Config();
        config.protocol = aliPayConfig.getProtocol();
        config.gatewayHost = aliPayConfig.getGatewayHost();
        config.signType = aliPayConfig.getSignType();
        config.appId = aliPayConfig.getAppId();
        config.merchantPrivateKey = aliPayConfig.getPrivateKey();
        config.alipayPublicKey = aliPayConfig.getPublicKey();
        config.notifyUrl = aliPayConfig.getNotifyUrl();
        return config;
    }
}
