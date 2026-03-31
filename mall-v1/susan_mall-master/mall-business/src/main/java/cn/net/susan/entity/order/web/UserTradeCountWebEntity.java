package cn.net.susan.entity.order.web;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel("用户订单数量web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTradeCountWebEntity {

    /**
     * 待支付订单数量
     */
    private int waitPayCount;

    /**
     * 已支付，待发货订单数量
     */
    private int payCount;

    /**
     * 已发货，待收货订单数量
     */
    private int shippedCount;

    /**
     * 已完成订单数量
     */
    private int finishCount;
}
