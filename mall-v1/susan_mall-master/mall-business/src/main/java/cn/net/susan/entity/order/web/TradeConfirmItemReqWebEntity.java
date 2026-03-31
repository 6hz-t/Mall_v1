package cn.net.susan.entity.order.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@ApiModel("订单确认请求项实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeConfirmItemReqWebEntity {

    /**
     * 购物车商品ID
     */
    @ApiModelProperty("购物车商品ID")
    private Long shoppingCartId;

    /**
     * 使用优惠券ID
     */
    @ApiModelProperty("使用优惠券ID")
    private Long couponId;

}
