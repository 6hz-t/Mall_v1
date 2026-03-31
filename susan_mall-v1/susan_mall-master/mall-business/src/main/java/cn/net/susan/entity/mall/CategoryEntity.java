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


@ApiModel("分类实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryEntity extends BaseEntity {


	/**
	 * 父分类ID
	 */
	@NotNull(message = "父分类ID不能为空")
	@ApiModelProperty("父分类ID")
	private Long parentId;

	/**
	 * 分类名称
	 */
	@ValidSensitiveWordField
	@NotEmpty(message = "分类名称不能为空")
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
