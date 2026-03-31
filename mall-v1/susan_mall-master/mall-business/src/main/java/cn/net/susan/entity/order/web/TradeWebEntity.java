package cn.net.susan.entity.order.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@ApiModel("订单Web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeWebEntity {

    /**
     * 系统ID
     */
    @ApiModelProperty("系统ID")
    private Long id;

    /**
     * 订单编码
     */
    @ApiModelProperty("订单编码")
    private String code;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date orderTime;

    /**
     * 订单状态 1:下单 2:支付 3：完成 4：取消
     */
    @ApiModelProperty("订单状态 1:下单 2:支付 3：完成 4：取消")
    private Integer orderStatus;

    /**
     * 支付状态 1:待支付 2:已支付 3：退款
     */
    @ApiModelProperty("支付状态 1:待支付 2:已支付 3：退款")
    private Integer payStatus;

    /**
     * 总金额
     */
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    /**
     * 付款金额
     */
    @ApiModelProperty("付款金额")
    private BigDecimal paymentAmount;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


    /**
     * 订单明细
     */
    @ApiModelProperty("订单明细")
    private List<TradeItemWebEntity> tradeItemEntityList;

    /**
     * 订单类型 1：普通商品订单 2：秒杀商品订单
     */
    @ApiModelProperty("订单类型 1：普通商品订单 2：秒杀商品订单")
    public Integer orderType;

    /**
     * 是否已评价
     */
    @ApiModelProperty("是否已评价")
    private Boolean isComment;
}
