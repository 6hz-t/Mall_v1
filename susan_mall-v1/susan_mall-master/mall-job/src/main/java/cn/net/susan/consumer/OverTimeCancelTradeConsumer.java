package cn.net.susan.consumer;

import cn.hutool.json.JSONUtil;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.service.order.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@RocketMQMessageListener(topic = "${mall.mgt.overTimeCancelTradeTopic:OVER_TIME_CANCEL_TRADE_TOPIC}",
        consumerGroup = "${mall.mgt.overTimeCancelTradeGroup:OVER_TIME_CANCEL_TRADE_GROUP}")
@Slf4j
@Component
public class OverTimeCancelTradeConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private TradeService tradeService;

    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String content = new String(body);
        log.info("OverTimeCancelTradeConsumer接收到消息：{}", content);
        TradeEntity tradeEntity = JSONUtil.toBean(content, TradeEntity.class);
        tradeService.handleOverTimeCancelTrade(tradeEntity);
    }
}
