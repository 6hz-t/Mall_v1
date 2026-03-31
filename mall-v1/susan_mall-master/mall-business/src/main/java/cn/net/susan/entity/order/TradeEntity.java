package cn.net.susan.entity.order;

import cn.net.susan.annotation.MaxMoney;
import cn.net.susan.annotation.MinMoney;
import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@ApiModel("订单实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeEntity extends BaseEntity {


    /**
     * 订单编码
     */
    @NotEmpty(message = "订单编码不能为空")
    @ApiModelProperty("订单编码")
    private String code;


    /**
     * 订单编码hash
     */
    @ApiModelProperty("订单编码hash")
    private Integer codeHash;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date orderTime;

    /**
     * 订单状态 1:已下单 2:已支付 3：已发货 4：已完成 5：已取消 6：已退货 7：已评价
     */
    @ApiModelProperty("订单状态 1:已下单 2:已支付 3：已发货 4：已完成 5：已取消 6：已退货 7：已评价")
    private Integer orderStatus;

    /**
     * 支付状态 1:待支付 2:已支付 3：退款
     */
    @ApiModelProperty("支付状态 1:待支付 2:已支付 3：退款")
    private Integer payStatus;

    /**
     * 总金额
     */
    @NotNull(message = "总金额不能为空")
    @ApiModelProperty("总金额")
    @MinMoney(value = 0, message = "总金额不能小于0")
    @MaxMoney(value = 100000, message = "总金额必须小于100000")
    private BigDecimal totalAmount;

    /**
     * 付款金额
     */
    @NotNull(message = "付款金额不能为空")
    @ApiModelProperty("付款金额")
    @MinMoney(value = 0, message = "付款金额不能小于0")
    @MaxMoney(value = 100000, message = "付款金额必须小于100000")
    private BigDecimal paymentAmount;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


    /**
     * 订单明细
     */
    @Valid
    @NotNull(message = "订单明细不能为空")
    @ApiModelProperty("订单明细")
    private List<TradeItemEntity> tradeItemEntityList;

    /**
     * 订单类型 1：普通商品订单 2：秒杀商品订单
     */
    @ApiModelProperty("订单类型 1：普通商品订单 2：秒杀商品订单")
    public Integer orderType;
}
