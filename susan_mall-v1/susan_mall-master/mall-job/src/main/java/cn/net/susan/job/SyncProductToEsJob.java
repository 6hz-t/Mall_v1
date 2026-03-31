package cn.net.susan.job;

import cn.net.susan.enums.JobResult;
import cn.net.susan.service.es.SyncProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.net.susan.enums.JobResult.SUCCESS;


@Slf4j
@Component
public class SyncProductToEsJob extends BaseJob {

    @Autowired
    private SyncProductService syncProductService;

    @Override
    public JobResult doRun(String params) {
        syncProductService.syncProductToES();
        return SUCCESS;
    }
}
