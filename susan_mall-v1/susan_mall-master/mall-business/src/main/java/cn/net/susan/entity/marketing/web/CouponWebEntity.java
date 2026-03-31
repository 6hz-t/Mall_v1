package cn.net.susan.entity.marketing.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;


@ApiModel("优惠券web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponWebEntity {

    /**
     * 优惠券发放系统ID
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty("优惠券发放系统ID")
    private Long id;

    /**
     * 优惠券系统ID
     */
    @ApiModelProperty("优惠券系统ID")
    private Long couponId;

    /**
     * 优惠券名称
     */
    @ApiModelProperty("优惠券名称")
    private String name;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

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
     * 类型 1：现金券 2：阶梯满减 3：每满减 4：通用折扣 5：满N件折扣 6：满Y元折扣
     */
    @ApiModelProperty("优惠券名称")
    private Integer type;

    /**
     * 图片地址
     */
    @ApiModelProperty("图片地址")
    private String photoUrl;

    /**
     * 领券开始时间
     */
    @ApiModelProperty("领券开始时间")
    private Date receiveStartTime;

    /**
     * 领券结束时间
     */
    @ApiModelProperty("领券结束时间")
    private Date receiveEndTime;

    /**
     * 使用开始时间
     */
    @ApiModelProperty("使用开始时间")
    private String useStartTimeStr;

    /**
     * 使用结束时间
     */
    @ApiModelProperty("使用结束时间")
    private String useEndTimeStr;

    /**
     * 优惠券总数量
     */
    @ApiModelProperty("优惠券总数量")
    private Integer quantity;

    /**
     * 已领取数量
     */
    @ApiModelProperty("已领取数量")
    private Integer receiveCount;

    /**
     * 优惠金额，比如：满100，减40， 这里就是40
     */
    @ApiModelProperty("优惠金额，比如：满100，减40， 这里就是40")
    private Integer offMoney;

    /**
     * 折扣，百分之多少，比如：8折，就填入80
     */
    @ApiModelProperty("折扣，百分之多少，比如：8折，就填入80")
    private Integer discount;

    /**
     * 最低使用金额，比如：满100，减40， 这里就是100
     */
    @ApiModelProperty("最低使用金额，比如：满100，减40， 这里就是100")
    private Integer minMoney;

    /**
     * 最少商品件数，比如：2件或者3件
     */
    @ApiModelProperty("最少商品件数，比如：2件或者3件")
    private Integer minProductCount;

    /**
     * 有效期天数
     */
    @ApiModelProperty("有效期天数")
    private Integer validDays;

    /**
     * 当前用户是否已领取
     */
    @ApiModelProperty("当前用户是否已领取")
    private boolean currentUserReceived;

    /**
     * 当前优惠券是否已使用
     */
    @ApiModelProperty("当前优惠券是否已使用")
    private boolean hasUsed;
}
