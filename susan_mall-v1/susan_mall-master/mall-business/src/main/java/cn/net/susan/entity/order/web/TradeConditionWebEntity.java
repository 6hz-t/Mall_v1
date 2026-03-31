package cn.net.susan.entity.order.web;

import cn.net.susan.entity.RequestConditionEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@ApiModel("订单查询条件实体")
@Data
public class TradeConditionWebEntity extends RequestConditionEntity {

    /**
     * 订单编码
     */
    @ApiModelProperty("订单编码")
    private String code;

    /**
     * 订单状态 1:下单 2:支付 3：完成 4：取消
     */
    @ApiModelProperty("订单状态 1:下单 2:支付 3：完成 4：取消")
    private Integer orderStatus;

}
