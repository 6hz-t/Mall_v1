package cn.net.susan.entity.marketing;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponUserProvideEntity extends CouponUserEntity {

    /**
     * 有效状态 1:有效 0:无效
     */
    private Integer validStatus;

    /**
     * 已领取数量
     */
    @ApiModelProperty("已领取数量")
    private Integer receiveCount;
}
