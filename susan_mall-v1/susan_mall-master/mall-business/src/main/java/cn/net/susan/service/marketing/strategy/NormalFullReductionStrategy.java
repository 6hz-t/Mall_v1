package cn.net.susan.service.marketing.strategy;

import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.enums.CouponTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class NormalFullReductionStrategy implements ICouponStrategy {
    @Override
    public CouponTypeEnum getType() {
        return CouponTypeEnum.NORMAL_FULL_REDUCTION;
    }

    @Override
    public BigDecimal calcPayMoney(BigDecimal money, CouponWebEntity couponEntity) {
        if (money.doubleValue() >= couponEntity.getMinMoney()) {
            return money.subtract(new BigDecimal(couponEntity.getOffMoney()));
        }
        return money;
    }
}
