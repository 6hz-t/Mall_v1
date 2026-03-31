package cn.net.susan.helper;

import cn.net.susan.entity.marketing.CouponConditionEntity;
import cn.net.susan.entity.marketing.CouponEntity;
import cn.net.susan.entity.marketing.CouponUserEntity;
import cn.net.susan.entity.marketing.CouponUserProvideEntity;
import cn.net.susan.mapper.marketing.CouponMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class CouponUserHelper {

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserProductHelper userProductHelper;

    /**
     * 填充优惠券和用户信息
     *
     * @param list 优惠券用户实体集合
     */
    public void fillCouponUserInfo(List<? extends CouponUserEntity> list) {
        fillCouponInfo(list);
        userProductHelper.fillUserProductInfo(list);
    }


    /**
     * 填充优惠券信息
     *
     * @param list 优惠券集合
     */
    public void fillCouponInfo(List<? extends CouponUserEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<Long> couponIdList = list.stream().map(CouponUserEntity::getCouponId).distinct().collect(Collectors.toList());
        CouponConditionEntity couponConditionEntity = new CouponConditionEntity();
        couponConditionEntity.setIdList(couponIdList);
        List<CouponEntity> couponEntities = couponMapper.searchByCondition(couponConditionEntity);
        for (CouponUserEntity couponUserEntity : list) {
            Optional<CouponEntity> couponEntityOptional = couponEntities.stream().filter(x -> x.getId().equals(couponUserEntity.getCouponId())).findAny();
            if (couponEntityOptional.isPresent()) {
                couponUserEntity.setCouponName(couponEntityOptional.get().getName());
            }
        }
    }
}
