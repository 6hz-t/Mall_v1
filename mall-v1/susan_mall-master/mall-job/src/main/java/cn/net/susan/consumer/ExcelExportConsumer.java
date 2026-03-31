package cn.net.susan.consumer;

import cn.hutool.json.JSONUtil;
import cn.net.susan.entity.common.CommonNotifyEntity;
import cn.net.susan.mapper.common.CommonNotifyMapper;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@RocketMQMessageListener(topic = "${mall.mgt.excelExportTopic:EXCEL_EXPORT_TOPIC}",
        consumerGroup = "${mall.mgt.excelExportGroup:EXCEL_EXPORT_GROUP}")
@Slf4j
@Component
public class ExcelExportConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private CommonNotifyMapper commonNotifyMapper;

    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String content = new String(body);
        log.info("ExcelExportConsumer接收到消息：{}", content);
        CommonNotifyEntity commonTaskEntity = JSONUtil.toBean(content, CommonNotifyEntity.class);
        pushNotify(commonTaskEntity);
    }


    private void pushNotify(CommonNotifyEntity commonNotifyEntity) {
        try {
            WebSocketServer.sendMessage(commonNotifyEntity);

            commonNotifyEntity.setIsPush(1);
            FillUserUtil.mockCurrentUser();
            commonNotifyMapper.update(commonNotifyEntity);
        } catch (IOException e) {
            log.error("WebSocket通知推送失败，原因：", e);
        } finally {
            FillUserUtil.clearCurrentUser();
        }
    }
}
