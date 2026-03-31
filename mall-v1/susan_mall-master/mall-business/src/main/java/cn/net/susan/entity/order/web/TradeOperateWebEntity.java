package cn.net.susan.entity.order.web;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@ApiModel("订单操作web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeOperateWebEntity {

    /**
     * 订单code
     */
    @NotEmpty(message = "订单Code不能为空")
    private String tradeCode;
}
