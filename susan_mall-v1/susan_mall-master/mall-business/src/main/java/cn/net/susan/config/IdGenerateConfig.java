package cn.net.susan.config;

import cn.net.susan.util.SnowFlakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class IdGenerateConfig {

    @Bean
    public SnowFlakeIdWorker snowFlakeIdWorker() {
        return new SnowFlakeIdWorker(0, 0);
    }
}
