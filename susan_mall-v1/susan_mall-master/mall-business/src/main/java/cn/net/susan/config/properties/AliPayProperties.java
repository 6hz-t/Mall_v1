package cn.net.susan.config.properties;

import lombok.Data;


@Data
public class AliPayProperties {

    private String protocol;
    private String gatewayHost;
    private String signType;
    private String appId;
    private String privateKey;
    private String publicKey;
    private String notifyUrl;
}
