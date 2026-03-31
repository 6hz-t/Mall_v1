package cn.net.susan.entity.mall.web;

import cn.net.susan.entity.EsBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;


@Document(indexName = "#{businessConfig.productEsIndexName}")
@ApiModel("商品web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductWebEntity extends EsBaseEntity {

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String model;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer quantity;

    /**
     * 剩余库存
     */
    @ApiModelProperty("剩余库存")
    private Integer remainQuantity;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    @Field(type = FieldType.Keyword)
    private String price;

    /**
     * 封面图片
     */
    @ApiModelProperty("封面图片")
    private String cover;

    /**
     * 商品类型
     */
    @ApiModelProperty("商品类型")
    private Integer productType;

    /**
     * 销量
     */
    @Field(type = FieldType.Keyword)
    private String saleQuantity;

    /**
     * 评价数量
     */
    private String commentCount;

    /**
     * 好评率
     */
    @Field(type = FieldType.Keyword)
    private String positiveRating;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private String totalAmount;
}
