package cn.net.susan.service.pay;

import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.order.TradeConditionEntity;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.entity.pay.web.PayWebEntity;
import cn.net.susan.enums.OrderStatusEnum;
import cn.net.susan.enums.PayStatusEnum;
import cn.net.susan.service.order.TradeService;
import cn.net.susan.util.FillUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;



@Slf4j
@Service
public class PayService {

    @Autowired
    private TradeService tradeService;

    /**
     * 模拟支付接口
     *
     * @param payWebEntity 参数
     * @return 是否支付成功
     */
    public Boolean mockPay(PayWebEntity payWebEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        if (Objects.isNull(currentUserInfo)) {
            log.warn("模拟支付失败，当前用户未登录");
            return Boolean.FALSE;
        }

        TradeConditionEntity tradeConditionEntity = new TradeConditionEntity();
        tradeConditionEntity.setCode(payWebEntity.getTradeCode());
        List<TradeEntity> tradeEntities = tradeService.searchByCondition(tradeConditionEntity);
        if (CollectionUtils.isEmpty(tradeEntities)) {
            log.warn("模拟支付失败，当前订单不存在");
            return Boolean.FALSE;
        }

        TradeEntity tradeEntity = tradeEntities.get(0);
        tradeEntity.setOrderStatus(OrderStatusEnum.PAY.getValue());
        tradeEntity.setPayStatus(PayStatusEnum.PAYMENT.getValue());
        FillUserUtil.fillUpdateUserInfo(tradeEntity);
        tradeService.update(tradeEntity);
        return Boolean.TRUE;
    }
}
