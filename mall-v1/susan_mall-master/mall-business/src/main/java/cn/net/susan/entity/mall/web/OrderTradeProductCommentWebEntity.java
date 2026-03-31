package cn.net.susan.entity.mall.web;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@ApiModel("订单商品评价实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderTradeProductCommentWebEntity {

    /**
     * 订单code
     */
    @NotEmpty(message = "订单Code不能为空")
    private String tradeCode;

    /**
     * 评价集合
     */
    @NotEmpty(message = "评价集合不能为空")
    private List<ProductCommentWebEntity> productCommentWebEntityList;
}
