package cn.net.susan.service.marketing.strategy;

import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.enums.CouponTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class CashReductionStrategy implements ICouponStrategy {
    @Override
    public CouponTypeEnum getType() {
        return CouponTypeEnum.CASH;
    }

    @Override
    public BigDecimal calcPayMoney(BigDecimal money, CouponWebEntity couponEntity) {
        if (money.doubleValue() >= couponEntity.getOffMoney()) {
            return money.subtract(new BigDecimal(couponEntity.getOffMoney()));
        }
        return BigDecimal.ZERO;
    }
}
