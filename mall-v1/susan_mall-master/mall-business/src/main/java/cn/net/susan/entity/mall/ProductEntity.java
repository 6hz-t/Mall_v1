package cn.net.susan.entity.mall;

import cn.net.susan.annotation.ValidSensitiveWordField;
import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@ApiModel("商品实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductEntity extends BaseProductEntity {

    /**
     * 商品组ID
     */
    @ApiModelProperty("商品组ID")
    private Long productGroupId;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 品牌ID
     */
    @NotNull(message = "品牌ID不能为空")
    @ApiModelProperty("品牌ID")
    private Long brandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;

    /**
     * 单位ID
     */
    @NotNull(message = "单位ID不能为空")
    @ApiModelProperty("单位ID")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String unitName;

    /**
     * 商品名称
     */
    @ValidSensitiveWordField
    @NotEmpty(message = "商品名称不能为空")
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 规格
     */
    @ValidSensitiveWordField
    @ApiModelProperty("规格")
    private String model;

    /**
     * 规格hash值
     */
    @ApiModelProperty("规格hash值")
    private String hash;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @ApiModelProperty("数量")
    private Integer quantity;

    /**
     * 剩余库存
     */
    @ApiModelProperty("剩余库存")
    private Integer remainQuantity;

    /**
     * 库存
     */
    @ApiModelProperty("库存")
    private Integer stock;

    /**
     * 价格
     */
    @NotNull(message = "数量不能为空")
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 封面图片url
     */
    @ApiModelProperty("封面图片url")
    private String coverUrl;

    /**
     * 商品组属性集合
     */
    @Size(message = "商品组集合不能为空")
    @ApiModelProperty("商品组集合")
    private List<AttributeValueEntity> spuAttributeEntityList;

    /**
     * 商品属性集合
     */
    @Size(message = "商品属性集合不能为空")
    @ApiModelProperty("商品属性集合")
    private List<AttributeValueEntity> skuAttributeEntityList;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private List<ProductPhotoEntity> productPhotoEntityList;

    /**
     * 是否新创建的商品
     */
    private Boolean isNew;

    /**
     * 属性值组合
     */
    private String attributeValueIds;

    /**
     * 封面图片
     */
    private List<String> cover;

    /**
     * 轮播图
     */
    private List<String> swiper;

    /**
     * 详情
     */
    private String detail;

    /**
     * 商品组实体
     */
    private ProductGroupEntity productGroupEntity;

    /**
     * 逻辑删除ID，默认是0，表示未删除
     */
    private Long delId;
}
