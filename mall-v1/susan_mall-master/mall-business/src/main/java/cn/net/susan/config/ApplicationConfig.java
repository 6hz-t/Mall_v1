package cn.net.susan.config;

import cn.net.susan.mybatis.DictCacheKeyGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@MapperScan(basePackages = "cn.net.susan.mapper")
@Configuration
public class ApplicationConfig {

    @Bean
    public DictCacheKeyGenerator dictCacheKeyGenerator() {
        return new DictCacheKeyGenerator();
    }
}
