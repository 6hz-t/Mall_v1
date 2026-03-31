package cn.net.susan.consumer;

import cn.hutool.json.JSONUtil;
import cn.net.susan.entity.common.CommonJobEntity;
import cn.net.susan.enums.CommonJobOperateTypeEnum;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.quartz.QuartzManage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@RocketMQMessageListener(topic = "${mall.mgt.commonJobTopic:COMMON_JOB_TOPIC}",
        consumerGroup = "${mall.mgt.commonJobGroup:COMMON_JOB_GROUP}")
@Slf4j
@Component
public class DynamicJobConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private QuartzManage quartzManage;


    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String content = new String(body);
        log.info("DynamicJobConsumer接收到消息：{}", content);
        CommonJobEntity commonJobEntity = JSONUtil.toBean(content, CommonJobEntity.class);
        handleDynamicJobMessage(commonJobEntity);
    }


    private void handleDynamicJobMessage(CommonJobEntity commonJobEntity) {
        CommonJobOperateTypeEnum operateTypeEnum = commonJobEntity.getOperateTypeEnum();
        switch (operateTypeEnum) {
            case NEW:
                quartzManage.addJob(commonJobEntity);
                break;
            case UPDATE:
                quartzManage.updateJobCron(commonJobEntity);
                break;
            case DELETE:
                quartzManage.deleteJob(commonJobEntity);
                break;
            case RUN_NOW:
                quartzManage.runJobNow(commonJobEntity);
                break;
            case PAUSE:
                quartzManage.pauseJob(commonJobEntity);
                break;
            case RESUME:
                quartzManage.resumeJob(commonJobEntity);
                break;
            default:
                throw new BusinessException("动态定时任务操作类型错误");
        }
    }
}
