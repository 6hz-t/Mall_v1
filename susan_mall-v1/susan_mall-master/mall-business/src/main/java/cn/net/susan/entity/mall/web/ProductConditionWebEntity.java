package cn.net.susan.entity.mall.web;

import cn.net.susan.entity.RequestPageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("商品查询条件实体")
@Data
public class ProductConditionWebEntity extends RequestPageEntity {


    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    private Long categoryId;

    /**
     * 品牌ID
     */
    @ApiModelProperty("品牌ID")
    private Long brandId;

    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    private Long unitId;


    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keyword;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private Integer type;
}
