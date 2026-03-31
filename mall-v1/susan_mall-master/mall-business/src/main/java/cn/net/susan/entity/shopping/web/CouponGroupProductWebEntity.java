package cn.net.susan.entity.shopping.web;

import cn.net.susan.entity.marketing.web.CouponWebEntity;
import lombok.Data;

import java.util.List;


@Data
public class CouponGroupProductWebEntity {

    /**
     * 使用优惠券
     */
    private CouponWebEntity couponWebEntity;

    /**
     * 购物车中的商品列表
     */
    private List<ShoppingCartProductWebEntity> shoppingCartList;


}
