package cn.net.susan.entity.sys;

import cn.net.susan.entity.BaseEntity;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("部门实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeptEntity extends BaseEntity {


	/**
	 * 名称
	 */
	@ExcelProperty(value = "部门名称", index = 0)
	@ApiModelProperty("名称")
	private String name;

	/**
	 * 上级部门
	 */
	@ExcelProperty(value = "上级部门ID", index = 1)
	@ApiModelProperty("上级部门")
	private Long pid;

	/**
	 * 有效状态 1:有效 0:无效
	 */
	@ExcelProperty(value = "有效状态", index = 2)
	@ApiModelProperty("有效状态 1:有效 0:无效")
	private Boolean validStatus;

	/**
	 * 角色ID
	 */
	private Long roleId;
}
