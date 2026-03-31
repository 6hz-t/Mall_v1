package cn.net.susan.entity.order.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@ApiModel("订单明细实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeItemWebEntity {

    /**
     * 系统ID
     */
    @ApiModelProperty("系统ID")
    private Long id;

    /**
     * 封面图片地址
     */
    @ApiModelProperty("封面图片地址")
    private String coverUrl;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty("商品ID")
    private Long productId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * 商品规格
     */
    @ApiModelProperty("商品规格")
    private String model;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    @ApiModelProperty("单价")
    private BigDecimal price;

    /**
     * 到手价
     */
    @ApiModelProperty("到手价")
    private BigDecimal payPrice;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @ApiModelProperty("数量")
    private Integer quantity;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @ApiModelProperty("金额")
    private BigDecimal amount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;
}
