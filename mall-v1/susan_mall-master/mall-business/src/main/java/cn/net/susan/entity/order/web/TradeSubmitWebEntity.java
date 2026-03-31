package cn.net.susan.entity.order.web;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@ApiModel("订单提交web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeSubmitWebEntity {
    /**
     * 订单确认code
     */
    @NotEmpty(message = "订单Code不能为空")
    private String tradeCode;

    /**
     * 收货地址ID
     */
    @NotNull(message = "收货地址ID不能为空")
    private Long deliveryAddressId;

    /**
     * 备注
     */
    private String remark;
}
