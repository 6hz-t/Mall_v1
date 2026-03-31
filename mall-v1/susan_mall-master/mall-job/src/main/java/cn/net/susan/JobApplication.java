package cn.net.susan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@SpringBootApplication(
        scanBasePackages = {"cn.net.susan"},
        excludeName = {"org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration"}
)
public class JobApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
}
