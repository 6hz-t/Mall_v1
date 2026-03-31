package cn.net.susan.entity.order.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@ApiModel("订单明细请求Web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeItemReqWebEntity {

    /**
     * 订单编码
     */
    @NotEmpty(message = "订单编码不能为空")
    @ApiModelProperty("订单编码")
    private String tradeCode;

    /**
     * 订单明细ID
     */
    @NotNull(message = "订单明细ID不能为空")
    @ApiModelProperty("订单明细ID")
    private Long tradeItemId;
}
