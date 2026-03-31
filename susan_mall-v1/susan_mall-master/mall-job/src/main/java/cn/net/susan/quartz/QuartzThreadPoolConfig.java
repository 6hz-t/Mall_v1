package cn.net.susan.quartz;

import cn.net.susan.config.BusinessConfig;
import cn.net.susan.config.properties.QuartzThreadPoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Configuration
public class QuartzThreadPoolConfig {

    @Autowired
    private BusinessConfig businessConfig;


    @Bean("quartzThreadPoolExecutor")
    public ThreadPoolExecutor quartzThreadPoolExecutor() {
        QuartzThreadPoolProperties quartzThreadPoolConfig = businessConfig.getQuartzThreadPoolConfig();
        ThreadPoolExecutor threadPoolTaskExecutor = new ThreadPoolExecutor(quartzThreadPoolConfig.getCorePoolSize(),
                quartzThreadPoolConfig.getMaxPoolSize(),
                quartzThreadPoolConfig.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(quartzThreadPoolConfig.getQueueSize()),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
