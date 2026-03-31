package cn.net.susan.entity.mall;

import cn.net.susan.annotation.ValidSensitiveWordField;
import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@ApiModel("属性值实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttributeValueEntity extends BaseEntity {


    /**
     * 属性ID
     */
    @NotNull(message = "属性ID不能为空 ")
    @ApiModelProperty("属性ID")
    private Long attributeId;

    /**
     * 属性名称
     */
    @ApiModelProperty("属性名称")
    private String attributeName;

    /**
     * 属性值
     */
    @NotEmpty(message = "属性值不能为空")
    @ValidSensitiveWordField
    @ApiModelProperty("属性值")
    private String value;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
}
