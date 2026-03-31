package cn.net.susan.entity.pay.web;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel("支付web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayWebEntity {
    /**
     * 订单code
     */
    private String tradeCode;
}
