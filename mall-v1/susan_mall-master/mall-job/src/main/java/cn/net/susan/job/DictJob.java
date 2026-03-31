package cn.net.susan.job;

import cn.net.susan.enums.JobResult;
import cn.net.susan.service.sys.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DictJob extends BaseJob {

    @Autowired
    private DictService dictService;

    @Override
    public JobResult doRun(String params) {
        dictService.refreshDict();
        return JobResult.SUCCESS;
    }
}
