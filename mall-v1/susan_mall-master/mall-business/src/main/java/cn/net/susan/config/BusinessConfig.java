package cn.net.susan.config;

import cn.net.susan.config.properties.AliPayProperties;
import cn.net.susan.config.properties.ProductDetailThreadPoolProperties;
import cn.net.susan.config.properties.QuartzThreadPoolProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@Slf4j
@ConfigurationProperties(prefix = "mall.mgt")
public class BusinessConfig {

    /**
     * 动态定时任务线程池配置
     */
    private QuartzThreadPoolProperties quartzThreadPoolConfig = new QuartzThreadPoolProperties();

    /**
     * 商品详情线程池配置
     */
    private ProductDetailThreadPoolProperties productDetailThreadPoolPoolConfig = new ProductDetailThreadPoolProperties();

    /**
     * 商品搜索index名称
     */
    private String productEsIndexName = "product-es-index-v1";

    /**
     * 秒杀商品搜索index名称
     */
    private String seckillProductEsIndexName = "seckill-product-es-index-v1";

    /**
     * 支付宝支付相关配置
     */
    private AliPayProperties aliPayConfig = new AliPayProperties();
}
