package cn.net.susan.service.order;

import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.order.TradeConditionEntity;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.enums.OrderStatusEnum;
import cn.net.susan.enums.PayStatusEnum;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.helper.IdGenerateHelper;
import cn.net.susan.helper.MqHelper;
import cn.net.susan.mapper.order.TradeItemMapper;
import cn.net.susan.mapper.order.TradeMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static cn.net.susan.config.RabbitConfig.OVER_TIME_CANCEL_QUEUE_ROUTING_KEY_PREFIX;


@Service
public class TradeSaveService {

    private static final int OVER_TIME_CANCEL_TRADE_DELAY_TIME = 30 * 60 * 1000;
    private static final int OVER_TIME_CANCEL_TRADE_DELAY_LEVEL = 16;

    @Autowired
    private TradeMapper tradeMapper;
    @Autowired
    private TradeItemMapper tradeItemMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private IdGenerateHelper idGenerateHelper;
    @Autowired
    private MqHelper mqHelper;
    @Value("${mall.mgt.overTimeCancelTradeTopic:OVER_TIME_CANCEL_TRADE_TOPIC}")
    private String overTimeCancelTradeTopic;


    @DS("sharding")
    public void createTrade(JwtUserEntity currentUserInfo, TradeEntity tradeEntity) {
        tradeEntity.setId(idGenerateHelper.nextId());
        tradeEntity.setUserId(currentUserInfo.getId());
        tradeEntity.setUserName(currentUserInfo.getUsername());
        tradeEntity.setOrderStatus(OrderStatusEnum.CREATE.getValue());
        tradeEntity.setPayStatus(PayStatusEnum.WAIT_PAY.getValue());
        tradeEntity.setOrderTime(new Date());

        transactionTemplate.execute((status) -> {
                    tradeMapper.insert(tradeEntity);
                    tradeEntity.getTradeItemEntityList().forEach(x -> {
                        x.setTradeId(tradeEntity.getId());
                        x.setCode(tradeEntity.getCode());
                    });
                    tradeItemMapper.batchInsert(tradeEntity.getTradeItemEntityList());
                    return Boolean.TRUE;
                }
        );

        //发送超时取消订单消息
        sendOvertimeCancelTradeMessage(tradeEntity);
    }

    private void sendOvertimeCancelTradeMessage(TradeEntity tradeEntity) {
        mqHelper.send(overTimeCancelTradeTopic,
                tradeEntity,
                OVER_TIME_CANCEL_TRADE_DELAY_LEVEL
        );
    }

    private String getOvertimeCancelTradeKey(Long id) {
        return String.format("%s%s", OVER_TIME_CANCEL_QUEUE_ROUTING_KEY_PREFIX, id);
    }

    @DS("sharding")
    public TradeEntity checkRepeat(TradeEntity tradeEntity, boolean exception) {
        if (!StringUtils.hasLength(tradeEntity.getCode())) {
            return null;
        }

        TradeConditionEntity tradeConditionEntity = new TradeConditionEntity();
        tradeConditionEntity.setCode(tradeEntity.getCode());
        List<TradeEntity> tradeEntities = tradeMapper.searchByCondition(tradeConditionEntity);
        if (CollectionUtils.isNotEmpty(tradeEntities)) {
            if (exception) {
                throw new BusinessException("该订单编号已存在");
            } else {
                return tradeEntities.get(0);
            }
        }
        return null;
    }

}
