package cn.net.susan.entity.order.web;

import cn.net.susan.entity.shopping.web.DeliveryAddressWebEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@ApiModel("订单详情web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeDetailWebEntity {

    /**
     * 订单编号
     */
    private String code;

    /**
     * 下单时间
     */
    private String orderTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 是否已评价
     */
    @ApiModelProperty("是否已评价")
    private Boolean isComment;

    /**
     * 收货地址
     */
    @ApiModelProperty("收货地址")
    private DeliveryAddressWebEntity deliveryAddressWebEntity;

    /**
     * 订单明细
     */
    @ApiModelProperty("订单明细")
    private List<TradeItemWebEntity> orderTradeItemList;

    /**
     * 订单优惠券实体
     */
    @ApiModelProperty("订单优惠券实体")
    private TradeCouponWebEntity tradeCouponWebEntity;

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

    /**
     * 商品件数
     */
    private int totalCount;
}
