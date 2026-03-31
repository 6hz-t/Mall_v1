package cn.net.susan.entity.mall;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;


@ApiModel("首页商品实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndexProductEntity extends BaseEntity {


    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long productId;

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
    private String cover;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 商品类型 1: 热门商品 2: 最新商品 3：秒杀商品
     */
    @ApiModelProperty("商品类型 1: 热门商品 2: 最新商品 3：秒杀商品")
    private Integer type;
}
