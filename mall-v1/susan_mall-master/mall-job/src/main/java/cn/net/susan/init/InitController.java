package cn.net.susan.init;

import cn.net.susan.service.common.CommonAreaService;
import cn.net.susan.service.common.CommonSensitiveWordService;
import cn.net.susan.service.es.SyncProductService;
import cn.net.susan.service.recommend.RecommendProductService;
import cn.net.susan.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.net.susan.util.FillUserUtil.mockCurrentUser;


@RestController
@RequestMapping("/init")
public class InitController {

    @Autowired
    private CommonSensitiveWordService commonSensitiveWordService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonAreaService commonAreaService;
    @Autowired
    private RecommendProductService recommendProductService;
    @Autowired
    private SyncProductService syncProductService;

    /**
     * 初始化敏感词
     */
    @GetMapping("/initSensitiveWord")
    public Boolean initSensitiveWord(@RequestParam("type") Integer type, @RequestParam("filePath") String filePath) {
        return mockCurrentUser(() -> commonSensitiveWordService.initSensitiveWord(type, filePath));
    }


    /**
     * 初始化商品到Redis
     */
    @GetMapping("/syncProductToES")
    public String syncProductToES() {
        syncProductService.syncProductToES();
        return "success";
    }

    /**
     * 初始化历史用户到Redis
     */
    @GetMapping("/initHistoryUserToRedis")
    public String initHistoryUserToRedis() {
        userService.initHistoryUserToRedis();
        return "success";
    }

    /**
     * 从国家统计局抓取省市县区的数据
     */
    @GetMapping("/initArea")
    public Boolean initArea() {
        return mockCurrentUser(() -> {
            commonAreaService.fetchData();
            return Boolean.TRUE;
        });
    }

    /**
     * 初始化推荐商品
     */
    @GetMapping("/initRecommendProduct")
    public Boolean initRecommendProduct() {
        recommendProductService.recommendProductToRedis();
        return Boolean.TRUE;
    }
}
