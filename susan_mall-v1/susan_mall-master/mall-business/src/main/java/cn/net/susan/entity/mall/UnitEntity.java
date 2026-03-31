package cn.net.susan.entity.mall;

import cn.net.susan.annotation.ValidSensitiveWordField;
import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("单位实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnitEntity extends BaseEntity {


    /**
     * 单位名称
     */
    @ValidSensitiveWordField
    @ApiModelProperty("单位名称")
    private String name;
}
