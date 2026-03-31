package cn.net.susan;

import cn.net.susan.config.ApplicationConfig;
import cn.net.susan.config.TestSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootApplication(scanBasePackages = {"cn.net.susan"})
@Import({ApplicationConfig.class, TestSecurityConfig.class})
@SpringBootTest
@ActiveProfiles("test")
public class SusanMallApplicationTests {

    /**
     * Spring Boot应用启动入口（用于测试环境）
     */
    public static void main(String[] args) {
        SpringApplication.run(SusanMallApplicationTests.class, args);
    }

    /**
     * 测试Spring上下文加载
     */
    @Test
    public void contextLoads() {
        // 验证Spring上下文能够正常加载
    }
}
