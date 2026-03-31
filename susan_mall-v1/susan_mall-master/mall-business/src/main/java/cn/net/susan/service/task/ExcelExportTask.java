package cn.net.susan.service.task;

import cn.hutool.json.JSONUtil;
import cn.net.susan.annotation.AsyncTask;
import cn.net.susan.entity.common.CommonNotifyEntity;
import cn.net.susan.entity.common.CommonTaskEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.enums.TaskStatusEnum;
import cn.net.susan.enums.TaskTypeEnum;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.helper.MqHelper;
import cn.net.susan.mapper.common.CommonNotifyMapper;
import cn.net.susan.mapper.common.CommonTaskMapper;
import cn.net.susan.service.BaseService;
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

import static cn.net.susan.config.RabbitConfig.EXCEL_EXPORT_EXCHANGE;
import static cn.net.susan.config.RabbitConfig.EXCEL_EXPORT_QUEUE_ROUTING_KEY_PREFIX;
import static cn.net.susan.constant.NumberConstant.NUMBER_3;


@AsyncTask(TaskTypeEnum.EXPORT_EXCEL)
@Slf4j
@Service
public class ExcelExportTask implements IAsyncTask {


    @Autowired
    private CommonTaskMapper commonTaskMapper;
    @Autowired
    private CommonNotifyMapper commonNotifyMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private MqHelper mqHelper;

    @Value("${mall.mgt.excelExportTopic:EXCEL_EXPORT_TOPIC}")
    private String excelExportTopic;


    @Override
    public void doTask(CommonTaskEntity commonTaskEntity) {
        doExportExcel(commonTaskEntity);
    }


    private void doExportExcel(CommonTaskEntity commonTaskEntity) {
        ExcelBizTypeEnum excelBizTypeEnum = getExcelBizTypeEnum(commonTaskEntity.getBizType());
        //任务开始执行时，状态改成执行中
        commonTaskEntity.setStatus(TaskStatusEnum.RUNNING.getValue());
        FillUserUtil.fillUpdateUserInfoFromCreate(commonTaskEntity);
        commonTaskMapper.update(commonTaskEntity);

        try {
            String requestEntity = excelBizTypeEnum.getRequestEntity();
            Class<?> aClass = null;
            try {
                aClass = Class.forName(requestEntity);
            } catch (ClassNotFoundException e) {
                log.error("数据导出异常，没有找到:{}", requestEntity);
                throw new BusinessException(String.format("数据导出异常，没有找到:%s", requestEntity));
            }
            String requestParam = commonTaskEntity.getRequestParam();
            Object toBean = JSONUtil.toBean(requestParam, aClass);
            String serviceName = this.getServiceName(requestEntity);
            BaseService baseService = (BaseService) SpringBeanUtil.getBean(serviceName);
            String fileName = getFileName(excelBizTypeEnum.getDesc());
            String fileUrl = baseService.export(toBean, fileName, this.getEntityName(requestEntity));
            //执行成功
            commonTaskEntity.setFileUrl(fileUrl);
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

        CommonNotifyEntity commonNotifyEntity = transactionTemplate.execute((status) -> {
            commonTaskMapper.update(commonTaskEntity);
            return saveNotifyMessage(commonTaskEntity);
        });

        mqHelper.send(excelExportTopic, commonNotifyEntity);

    }

    private ExcelBizTypeEnum getExcelBizTypeEnum(Integer bizType) {
        for (ExcelBizTypeEnum value : ExcelBizTypeEnum.values()) {
            if (value.getValue().equals(bizType)) {
                return value;
            }
        }
        throw new BusinessException("bizType非法");
    }

    private String getRoutingKey(Long id) {
        return String.format("%s%s", EXCEL_EXPORT_QUEUE_ROUTING_KEY_PREFIX, id);
    }


    private CommonNotifyEntity saveNotifyMessage(CommonTaskEntity commonTaskEntity) {
        CommonNotifyEntity commonNotifyEntity = new CommonNotifyEntity();
        commonNotifyEntity.setTitle("excel导出通知");
        commonNotifyEntity.setContent(getContent(commonTaskEntity));
        commonNotifyEntity.setToUserId(commonTaskEntity.getCreateUserId());
        commonNotifyEntity.setIsPush(0);
        commonNotifyEntity.setType(1);
        commonNotifyEntity.setReadStatus(0);
        commonNotifyEntity.setCreateUserId(commonTaskEntity.getCreateUserId());
        commonNotifyEntity.setCreateUserName(commonTaskEntity.getCreateUserName());
        commonNotifyEntity.setCreateTime(new Date());
        commonNotifyEntity.setIsDel(0);
        commonNotifyMapper.insert(commonNotifyEntity);
        return commonNotifyEntity;
    }

    private String getContent(CommonTaskEntity commonTaskEntity) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("<div>成功导出excel文件：")
                .append(commonTaskEntity.getName())
                .append("，文件地址：<a href='")
                .append(commonTaskEntity.getFileUrl())
                .append("' target='_blank' rel='noopener noreferrer'>")
                .append("<strong><u>点击下载</u></strong>")
                .append("</a></div>");
        return contentBuilder.toString();
    }

    private String getFileName(String fileName) {
        return String.format("%s数据_%s", fileName, DateFormatUtil.nowForFile());
    }


    private String getServiceName(String requestEntity) {
        String[] values = requestEntity.split("\\.");
        if (ObjectUtils.isEmpty(values)) {
            throw new BusinessException(String.format("requestEntity:%s实体格式不正常", requestEntity));
        }
        String entityName = values[values.length - 1];
        String prefix = entityName.substring(0, entityName.indexOf("ConditionEntity"));
        return prefix.substring(0, 1).toLowerCase() + prefix.substring(1) + "Service";
    }

    private String getEntityName(String requestEntity) {
        String[] values = requestEntity.split("\\.");
        if (ObjectUtils.isEmpty(values)) {
            throw new BusinessException(String.format("requestEntity:%s实体格式不正常", requestEntity));
        }
        String entityName = values[values.length - 1];
        entityName = entityName.replace("ConditionEntity", "Entity");

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < values.length - 1; i++) {
            nameBuilder.append(values[i]).append(".");
        }
        nameBuilder.append(entityName);
        return nameBuilder.toString();
    }
}
