package cn.net.susan.entity.mall.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel("分类实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryWebEntity {

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    private Long id;

    /**
     * 父分类ID
     */
    @ApiModelProperty("父分类ID")
    private Long parentId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 层级
     */
    @ApiModelProperty("层级")
    private Integer level;

    /**
     * 是否叶子节点
     */
    @ApiModelProperty("是否叶子节点")
    private Integer isLeaf;
}
