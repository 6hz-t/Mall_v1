package cn.net.susan.entity.mongo;

import cn.net.susan.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "ProductDetailEntity")
@ApiModel("商品详情实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailEntity extends BaseEntity {

    /**
     * 商品ID
     */
    @Indexed
    @ApiModelProperty("商品ID")
    private Long productId;

    /**
     * 商品详情
     */
    @ApiModelProperty("商品详情")
    private String detail;
}
