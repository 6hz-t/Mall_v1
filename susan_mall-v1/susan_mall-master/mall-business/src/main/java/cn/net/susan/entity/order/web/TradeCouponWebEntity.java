package cn.net.susan.entity.order.web;

import cn.net.susan.entity.marketing.web.CouponWebEntity;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@ApiModel("订单优惠券web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeCouponWebEntity {

    /**
     * 可使用优惠券列表
     */
    private List<CouponWebEntity> canUseCouponList;

    /**
     * 不可使用优惠券列表
     */
    private List<CouponWebEntity> unCanUseCouponList;

    /**
     * 优惠券总数量
     */
    private int totalCount;
}
