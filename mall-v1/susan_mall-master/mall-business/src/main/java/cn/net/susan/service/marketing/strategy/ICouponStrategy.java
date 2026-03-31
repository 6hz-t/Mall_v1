package cn.net.susan.service.marketing.strategy;

import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.enums.CouponTypeEnum;

import java.math.BigDecimal;


public interface ICouponStrategy {

    /**
     * 获取优惠券类型
     *
     * @return
     */
    CouponTypeEnum getType();

    /**
     * 使用优惠券后计算支付金额
     *
     * @param money        原始金额
     * @param couponEntity 优惠券实体
     * @return 优惠后的支付金额
     */
    BigDecimal calcPayMoney(BigDecimal money, CouponWebEntity couponEntity);
}
