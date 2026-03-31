package cn.net.susan.config;

import cn.net.susan.config.properties.ProductDetailThreadPoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Configuration
public class ThreadPoolConfig {

    @Autowired
    private BusinessConfig businessConfig;


    /**
     * 商品详情线程池
     *
     * @return 线程池
     */
    @Bean(name = "productDetailThreadPoolExecutor")
    public ThreadPoolExecutor productDetailThreadPoolExecutor() {
        ProductDetailThreadPoolProperties productDetailThreadPoolPoolConfig = businessConfig.getProductDetailThreadPoolPoolConfig();
        ThreadPoolExecutor threadPoolTaskExecutor = new ThreadPoolExecutor(productDetailThreadPoolPoolConfig.getCorePoolSize(),
                productDetailThreadPoolPoolConfig.getMaxPoolSize(),
                productDetailThreadPoolPoolConfig.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(productDetailThreadPoolPoolConfig.getQueueSize()),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
