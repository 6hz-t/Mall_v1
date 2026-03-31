package cn.net.susan.entity.shopping.web;

import cn.net.susan.entity.marketing.web.CouponWebEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingCartProductWebEntity {

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 系统ID
     */
    private Long id;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String model;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 到手价
     */
    @ApiModelProperty("到手价")
    private BigDecimal payPrice;

    /**
     * 封面
     */
    @ApiModelProperty("封面")
    private String cover;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否选中
     */
    private boolean checked;
}
