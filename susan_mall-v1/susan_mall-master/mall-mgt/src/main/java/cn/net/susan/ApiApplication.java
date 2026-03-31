package cn.net.susan;

import cn.net.susan.enable.EnableLimit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy(exposeProxy = true)
@EnableLimit
@EnableCaching
@SpringBootApplication(
        scanBasePackages = {"cn.net.susan"},
        excludeName = {"org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration"}
)
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
