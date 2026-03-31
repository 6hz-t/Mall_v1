package cn.net.susan.entity.sys;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("部门实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DictDetailEntity extends BaseEntity {


    /**
     * 数据字典id
     */
    @ApiModelProperty("数据字典id")
    private Long dictId;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 文本
     */
    @ApiModelProperty("文本")
    private String label;

    /**
     * 数据字典
     */
    @ApiModelProperty("数据字典")
    private DictEntity dict;
}
