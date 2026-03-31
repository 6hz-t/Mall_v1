package cn.net.susan.entity.aftersale.web;

import cn.net.susan.entity.RequestConditionEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("退货查询条件实体")
@Data
public class RefundConditionWebEntity extends RequestConditionEntity {

    /**
     * 退货类型
     */
    private Integer refundType;
}
