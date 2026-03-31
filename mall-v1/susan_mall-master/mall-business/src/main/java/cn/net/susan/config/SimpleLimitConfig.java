package cn.net.susan.config;

import cn.net.susan.interceptor.SimpleLimitAspect;
import org.springframework.context.annotation.Bean;


public class SimpleLimitConfig {

    @Bean
    public SimpleLimitAspect simpleLimitAspect() {
        return new SimpleLimitAspect();
    }
}
