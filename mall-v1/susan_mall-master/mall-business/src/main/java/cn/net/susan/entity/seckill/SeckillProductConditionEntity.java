package cn.net.susan.entity.seckill;

import cn.net.susan.entity.RequestConditionEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@ApiModel("秒杀商品查询条件实体")
@Data
public class SeckillProductConditionEntity extends RequestConditionEntity {


    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * ID集合
     */
    private List<Long> idList;

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long productId;

    /**
     * 预扣库存
     */
    @ApiModelProperty("预扣库存")
    private Integer withHoldQuantity;

    /**
     * 实际剩余库存
     */
    @ApiModelProperty("实际剩余库存")
    private Integer remainQuantity;

    /**
     * 秒杀价格
     */
    @ApiModelProperty("秒杀价格")
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
     * 秒杀开始时间
     */
    @ApiModelProperty("秒杀开始时间")
    private Date startTime;

    /**
     * 秒杀结束时间
     */
    @ApiModelProperty("秒杀结束时间")
    private Date endTime;
}
