package cn.net.susan.service.marketing.strategy;

import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.enums.CouponTypeEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static cn.net.susan.constant.NumberConstant.NUMBER_100;


@Component
public class FullAmountDiscountStrategy implements ICouponStrategy {
    @Override
    public CouponTypeEnum getType() {
        return CouponTypeEnum.FULL_AMOUNT_DISCOUNT;
    }

    @Override
    public BigDecimal calcPayMoney(BigDecimal money, CouponWebEntity couponEntity) {
        if (money.doubleValue() >= couponEntity.getMinMoney()) {
            return money.multiply(new BigDecimal(couponEntity.getDiscount())).divide(new BigDecimal(NUMBER_100));
        }
        return money;
    }
}
