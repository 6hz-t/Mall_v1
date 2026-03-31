package cn.net.susan.entity.marketing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponUserReceiveEntity extends CouponUserEntity {

    /**
     * 使用状态 1:已使用 0:未使用
     */
    private Integer useStatus;

    /**
     * 使用时间
     */
    private Date useTime;

    /**
     * 优惠券实体
     */
    private CouponEntity couponEntity;

    /**
     * 优惠券发放实体
     */
    private CouponUserProvideEntity couponUserProvideEntity;
}
