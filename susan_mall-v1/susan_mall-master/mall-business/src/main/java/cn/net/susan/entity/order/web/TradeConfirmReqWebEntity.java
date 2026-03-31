package cn.net.susan.entity.order.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@ApiModel("订单确认请求实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeConfirmReqWebEntity {

    /**
     * 购物车商品集合
     */
    @NotEmpty(message = "tradeConfirmItemReqWebEntityList不能为空")
    @ApiModelProperty("购物车商品集合")
    private List<TradeConfirmItemReqWebEntity> tradeConfirmItemReqWebEntityList;

}
