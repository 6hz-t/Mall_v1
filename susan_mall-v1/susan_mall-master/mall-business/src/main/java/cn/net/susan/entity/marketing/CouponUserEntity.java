package cn.net.susan.entity.marketing;

import cn.net.susan.entity.mall.UserProductEntity;
import lombok.Data;


@Data
public class CouponUserEntity extends UserProductEntity {

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 优惠券名称
     */
    private String couponName;
}
