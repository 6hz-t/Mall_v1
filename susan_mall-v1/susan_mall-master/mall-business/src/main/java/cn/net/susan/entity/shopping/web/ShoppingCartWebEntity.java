package cn.net.susan.entity.shopping.web;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class ShoppingCartWebEntity {

    /**
     * 购物车中的优惠券商品列表
     */
    private List<CouponGroupProductWebEntity> couponGroupProductWebEntityList;

    /**
     * 总金额
     */
    private BigDecimal totalMoney = BigDecimal.ZERO;

    /**
     * 最终支付金额
     */
    private BigDecimal finalMoney = BigDecimal.ZERO;

    /**
     * 优惠金额
     */
    private BigDecimal subtractMoney = BigDecimal.ZERO;
}
