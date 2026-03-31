package cn.net.susan.util;

import cn.hutool.core.bean.BeanUtil;
import cn.net.susan.entity.marketing.CouponEntity;
import cn.net.susan.entity.marketing.web.CouponWebEntity;


public abstract class CouponUtil {

    private CouponUtil() {

    }

    /**
     * 将CouponEntity对象转换成CouponWebEntity对象
     *
     * @param couponEntity 优惠券实体
     * @return 优惠券web实体
     */
    public static CouponWebEntity createCouponWebEntity(CouponEntity couponEntity) {
        CouponWebEntity couponWebEntity = new CouponWebEntity();
        BeanUtil.copyProperties(couponEntity, couponWebEntity, false);
        return couponWebEntity;
    }
}
