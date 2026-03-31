package cn.net.susan.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;



@Slf4j
@Component
public class MqHelper {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 发送RocketMQ消息
     *
     * @param topic 主题
     * @param data  消息
     */
    public void send(String topic, Object data, int delayLevel) {
        try {
            MessageHeaders headers = new MessageHeaders(
                    Collections.singletonMap(
                            MessageConst.PROPERTY_DELAY_TIME_LEVEL, String.valueOf(delayLevel)
                    )
            );
            org.springframework.messaging.Message<Object> message = MessageBuilder.createMessage(data, headers);
            rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("延迟消息发送成功, topic:{},message:{}", topic, data);
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("延迟消息发送失败, topic:{},throwable:{}", topic, throwable);
                }
            }, 3000, delayLevel);

        } catch (Exception e) {
            log.error("延迟消息发送失败，原因：", e);
        }
    }


    /**
     * 发送RocketMQ消息
     *
     * @param topic   主题
     * @param message 消息
     */
    public void send(String topic, Object message) {
        try {
            rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("消息发送成功, topic:{},message:{}", topic, message);
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("消息发送失败, topic:{},throwable:{}", topic, throwable);
                }
            });

        } catch (Exception e) {
            log.error("消息发送失败，原因：", e);
        }
    }

    /**
     * 发生MQ消息
     *
     * @param routingKey 路由key
     * @param message    消息
     */
    public void send(String routingKey, Message message) {
        rabbitTemplate.send(routingKey, message);
    }

    /**
     * 发生MQ消息
     *
     * @param exchange   交换机
     * @param routingKey 路由key
     * @param data       消息
     */
    public void send(String exchange, String routingKey, Object data) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, data);
            log.info("消息发送成功, exchange:{},routingKey:{},message:{}", exchange, routingKey, data);
        } catch (Exception e) {
            log.error("消息发送失败，原因：", e);
        }
    }

    /**
     * 发送MQ延迟消息
     *
     * @param exchange   交换机
     * @param routingKey 路由key
     * @param data       消息
     * @param delayTime  延迟时间，毫秒
     */
    public void sendDelayMessage(String exchange, String routingKey, Object data, int delayTime) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, data, message -> {
                message.getMessageProperties().setDelay(delayTime);
                return message;
            });
            log.info("延迟消息发送成功, exchange:{},routingKey:{},message:{}", exchange, routingKey, data);
        } catch (Exception e) {
            log.error("延迟消息发送失败，原因：", e);
        }
    }
}
