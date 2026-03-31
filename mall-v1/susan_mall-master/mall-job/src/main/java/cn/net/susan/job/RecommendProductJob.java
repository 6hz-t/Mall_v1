package cn.net.susan.job;

import cn.net.susan.enums.JobResult;
import cn.net.susan.service.recommend.RecommendProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.net.susan.enums.JobResult.SUCCESS;


@Slf4j
@Component
public class RecommendProductJob extends BaseJob {

    @Autowired
    private RecommendProductService recommendProductService;

    @Override
    public JobResult doRun(String params) {
        recommendProductService.recommendProductToRedis();
        return SUCCESS;
    }
}
