package cn.net.susan.service.task;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import cn.net.susan.annotation.AsyncTask;
import cn.net.susan.entity.common.CommonNotifyEntity;
import cn.net.susan.entity.common.CommonTaskEntity;
import cn.net.susan.entity.email.RemoteLoginEmailEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.enums.TaskStatusEnum;
import cn.net.susan.enums.TaskTypeEnum;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.mapper.common.CommonNotifyMapper;
import cn.net.susan.mapper.common.CommonTaskMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.service.email.RemoteLoginEmailService;
import cn.net.susan.util.DateFormatUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

import java.util.Date;

import static cn.net.susan.constant.NumberConstant.NUMBER_3;


@AsyncTask(TaskTypeEnum.SEND_EMAIL)
@Slf4j
@Service
public class SendEmailTask implements IAsyncTask {

    @Value("${mall.mgt.sendEmailOff:true}")
    private Boolean sendEmailOff;

    @Autowired
    private CommonTaskMapper commonTaskMapper;
    @Autowired
    private RemoteLoginEmailService remoteLoginEmailService;


    @Override
    public void doTask(CommonTaskEntity commonTaskEntity) {
        if (BooleanUtil.isTrue(sendEmailOff)) {
            return;
        }
        doSendEmail(commonTaskEntity);
    }


    private void doSendEmail(CommonTaskEntity commonTaskEntity) {
        //任务开始执行时，状态改成执行中
        commonTaskEntity.setStatus(TaskStatusEnum.RUNNING.getValue());
        FillUserUtil.fillUpdateUserInfoFromCreate(commonTaskEntity);
        commonTaskMapper.update(commonTaskEntity);

        try {
            RemoteLoginEmailEntity remoteLoginEmailEntity = JSONUtil.toBean(commonTaskEntity.getRequestParam(), RemoteLoginEmailEntity.class);
            remoteLoginEmailService.sendEmail(remoteLoginEmailEntity);
            commonTaskEntity.setStatus(TaskStatusEnum.SUCCESS.getValue());
        } catch (Exception e) {
            log.error("数据导出异常，原因：", e);
            //失败次数加1
            commonTaskEntity.setFailureCount(commonTaskEntity.getFailureCount() + 1);
            //如果失败次数超过3次，则将状态改成失败，后面不再执行
            if (commonTaskEntity.getFailureCount() >= NUMBER_3) {
                commonTaskEntity.setStatus(TaskStatusEnum.FAIL.getValue());
            }
        }

        commonTaskEntity.setUpdateTime(new Date());
        commonTaskMapper.update(commonTaskEntity);
    }

}
