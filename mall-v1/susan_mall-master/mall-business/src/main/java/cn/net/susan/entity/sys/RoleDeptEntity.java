package cn.net.susan.entity.sys;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("角色部门关联实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDeptEntity extends BaseEntity {
	

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long roleId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long deptId;
}
