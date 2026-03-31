package cn.net.susan.job;

import cn.net.susan.entity.common.CommonTaskConditionEntity;
import cn.net.susan.entity.common.CommonTaskEntity;
import cn.net.susan.enums.JobResult;
import cn.net.susan.enums.TaskStatusEnum;
import cn.net.susan.factory.AsyncTaskStrategyContextFactory;
import cn.net.susan.mapper.common.CommonTaskMapper;
import cn.net.susan.util.FillUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class CommonTaskJob extends BaseJob {

    private static final List<Integer> QUERY_VALID_STATUS_LIST = new ArrayList<>();

    static {
        QUERY_VALID_STATUS_LIST.add(TaskStatusEnum.WAITING.getValue());
        QUERY_VALID_STATUS_LIST.add(TaskStatusEnum.RUNNING.getValue());
    }

    @Autowired
    private CommonTaskMapper commonTaskMapper;

    /**
     * 定时执行
     */
    @Override
    public JobResult doRun(String params) {
        doRun();
        return JobResult.SUCCESS;
    }

    private void doRun() {
        CommonTaskConditionEntity commonTaskConditionEntity = new CommonTaskConditionEntity();
        commonTaskConditionEntity.setStatusList(QUERY_VALID_STATUS_LIST);
        List<CommonTaskEntity> commonTaskEntities = commonTaskMapper.searchByCondition(commonTaskConditionEntity);
        if (CollectionUtils.isEmpty(commonTaskEntities)) {
            log.info("没有任务需要执行");
            return;
        }

        for (CommonTaskEntity commonTaskEntity : commonTaskEntities) {
            AsyncTaskStrategyContextFactory.getInstance().getStrategy(commonTaskEntity.getType()).doTask(commonTaskEntity);
        }
    }
}
