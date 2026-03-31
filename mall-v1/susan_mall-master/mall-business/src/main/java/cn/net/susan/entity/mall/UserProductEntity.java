package cn.net.susan.entity.mall;

import cn.net.susan.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class UserProductEntity extends BaseEntity {

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
     * 封面
     */
    @ApiModelProperty("封面")
    private String coverUrl;

    /**
     * 库存
     */
    private Integer stock;
}
