package cn.net.susan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Configuration
public class RabbitConfig {

    /**
     * excel导出交换机
     */
    public static final String EXCEL_EXPORT_EXCHANGE = "excel_export_exchange";
    /**
     * excel导出队列
     */
    public static final String EXCEL_EXPORT_QUEUE = "excel_export_queue";

    /**
     * excel导出路由key前缀
     */
    public static final String EXCEL_EXPORT_QUEUE_ROUTING_KEY_PREFIX = "excel_export.";

    /**
     * excel导出路由key
     */
    public static final String EXCEL_EXPORT_QUEUE_ROUTING_KEY = EXCEL_EXPORT_QUEUE_ROUTING_KEY_PREFIX + "#";

    /**
     * 取消超时订单交换机
     */
    public static final String OVER_TIME_CANCEL_TRADE_EXCHANGE = "over_time_cancel_trade_exchange";
    /**
     * 取消超时订单队列
     */
    public static final String OVER_TIME_CANCEL_TRADE_QUEUE = "over_time_cancel_trade_queue";

    /**
     * 取消超时订单路由key前缀
     */
    public static final String OVER_TIME_CANCEL_QUEUE_ROUTING_KEY_PREFIX = "over_time_cancel_trade.";

    /**
     * 取消超时订单路由key
     */
    public static final String OVER_TIME_CANCEL_QUEUE_ROUTING_KEY = OVER_TIME_CANCEL_QUEUE_ROUTING_KEY_PREFIX + "#";

    /**
     * 订单状态变更交换机
     */
    public static final String TRADE_STATUS_CHANGE_EXCHANGE = "trade_status_change_exchange";
    /**
     * 订单状态变更队列
     */
    public static final String TRADE_STATUS_CHANGE_QUEUE = "trade_status_change_queue";

    /**
     * 订单状态变更路由key前缀
     */
    public static final String TRADE_STATUS_CHANGE_ROUTING_KEY_PREFIX = "trade_status_change.";

    /**
     * 订单状态变更路由key
     */
    public static final String TRADE_STATUS_CHANGE_ROUTING_KEY = TRADE_STATUS_CHANGE_ROUTING_KEY_PREFIX + "#";

    /**
     * 动态定时任务交换机
     */
    public static final String DYNAMIC_JOB_EXCHANGE = "dynamic_job_exchange";
    /**
     * 动态定时任务队列
     */
    public static final String DYNAMIC_JOB_QUEUE = "dynamic_job_queue";

    /**
     * 动态定时任务路由key前缀
     */
    public static final String DYNAMIC_JOB_ROUTING_KEY_PREFIX = "dynamic_job.";

    /**
     * 动态定时任务路由key
     */
    public static final String DYNAMIC_JOB_ROUTING_KEY = DYNAMIC_JOB_ROUTING_KEY_PREFIX + "#";

    /**
     * 延迟时间 （单位：ms(毫秒)）
     */
    public static final Integer DELAY_TIME = 10000;

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }


    /**
     * 创建excel导出交换机
     */
    @Bean("excelExportExchange")
    public Exchange excelExportExchange() {
        return new TopicExchange(EXCEL_EXPORT_EXCHANGE, true, false);
    }


    /**
     * 创建excel导出队列
     */
    @Bean("excelExportQueue")
    public Queue excelExportQueue() {
        Map<String, Object> args = new HashMap<>(1);
        //过期时间，单位毫秒
        args.put("x-message-ttl", DELAY_TIME);
        return QueueBuilder.durable(EXCEL_EXPORT_QUEUE).withArguments(args).build();
    }

    /**
     * 绑定excel导出交换机和队列
     */
    @Bean("excelExportBinding")
    public Binding excelExportBinding(@Qualifier("excelExportQueue") Queue queue,
                                      @Qualifier("excelExportExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(EXCEL_EXPORT_QUEUE_ROUTING_KEY).noargs();
    }

    /**
     * 创建取消超时订单交换机
     */
    @Bean("overtimeCancelTradeExchange")
    public Exchange overtimeCancelTradeExchange() {
        return new TopicExchange(OVER_TIME_CANCEL_TRADE_EXCHANGE, true, false);
    }


    /**
     * 创建取消超时订单队列
     */
    @Bean("overtimeCancelTradeQueue")
    public Queue overtimeCancelTradeQueue() {
        Map<String, Object> args = new HashMap<>(1);
        //过期时间，单位毫秒
        args.put("x-message-ttl", DELAY_TIME);
        return QueueBuilder.durable(OVER_TIME_CANCEL_TRADE_QUEUE).withArguments(args).build();
    }

    /**
     * 绑定取消超时订单交换机和队列
     */
    @Bean("overtimeCancelTradeBinding")
    public Binding overtimeCancelTradeBinding(@Qualifier("overtimeCancelTradeQueue") Queue queue,
                                              @Qualifier("overtimeCancelTradeExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(OVER_TIME_CANCEL_QUEUE_ROUTING_KEY).noargs();
    }

    /**
     * 创建订单状态变更交换机
     */
    @Bean("tradeStatusChangeExchange")
    public Exchange tradeStatusChangeExchange() {
        return new TopicExchange(TRADE_STATUS_CHANGE_EXCHANGE, true, false);
    }


    /**
     * 创建订单状态变更队列
     */
    @Bean("tradeStatusChangeQueue")
    public Queue tradeStatusChangeQueue() {
        Map<String, Object> args = new HashMap<>(1);
        //过期时间，单位毫秒
        args.put("x-message-ttl", DELAY_TIME);
        return QueueBuilder.durable(TRADE_STATUS_CHANGE_QUEUE).withArguments(args).build();
    }

    /**
     * 绑定订单状态变更交换机和队列
     */
    @Bean("tradeStatusChangeBinding")
    public Binding tradeStatusChangeBinding(@Qualifier("tradeStatusChangeQueue") Queue queue,
                                            @Qualifier("tradeStatusChangeExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(TRADE_STATUS_CHANGE_ROUTING_KEY).noargs();
    }

    /**
     * 创建动态定时任务交换机
     */
    @Bean("dynamicJobExchange")
    public Exchange dynamicJobExchange() {
        return new TopicExchange(DYNAMIC_JOB_EXCHANGE, true, false);
    }


    /**
     * 创建动态定时任务队列
     */
    @Bean("dynamicJobQueue")
    public Queue dynamicJobQueue() {
        Map<String, Object> args = new HashMap<>(1);
        //过期时间，单位毫秒
        args.put("x-message-ttl", DELAY_TIME);
        return QueueBuilder.durable(DYNAMIC_JOB_QUEUE).withArguments(args).build();
    }

    /**
     * 绑定动态定时任务交换机和队列
     */
    @Bean("dynamicJobBinding")
    public Binding dynamicJobBinding(@Qualifier("dynamicJobQueue") Queue queue,
                                            @Qualifier("dynamicJobExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DYNAMIC_JOB_ROUTING_KEY).noargs();
    }

    @Bean
    public MessageConverter jsonToMapMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
