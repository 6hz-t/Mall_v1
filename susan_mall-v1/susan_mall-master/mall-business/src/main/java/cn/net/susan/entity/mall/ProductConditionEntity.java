package cn.net.susan.entity.mall;

import cn.net.susan.entity.RequestConditionEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@ApiModel("商品查询条件实体")
@Data
public class ProductConditionEntity extends RequestConditionEntity {


    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * ID集合
     */
    @ApiModelProperty("ID集合")
    private List<Long> idList;

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
     * hash值
     */
    @ApiModelProperty("hash值")
    private String hash;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer quantity;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private Long createUserId;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty("修改人ID")
    private Long updateUserId;

    /**
     * 修改人名称
     */
    @ApiModelProperty("修改人名称")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否删除 1：已删除 0：未删除
     */
    @ApiModelProperty("是否删除 1：已删除 0：未删除")
    private Integer isDel;

    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keyword;

    /**
     * 商品查询条件
     */
    @ApiModelProperty("商品查询条件")
    private List<ProductEntity> productEntities;

    /**
     * 商品组ID
     */
    @ApiModelProperty("商品组ID")
    private Long productGroupId;

    /**
     * 商品组ID集合
     */
    @ApiModelProperty("商品组ID集合")
    private List<Long> productGroupIdList;

    /**
     * 逻辑删除ID，默认是0，表示未删除
     */
    private Long delId;
}
