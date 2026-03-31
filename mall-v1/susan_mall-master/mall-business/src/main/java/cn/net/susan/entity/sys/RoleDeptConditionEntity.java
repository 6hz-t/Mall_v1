package cn.net.susan.entity.sys;

import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("角色部门关联查询条件实体")
@Data
public class RoleDeptConditionEntity extends RequestPageEntity {
	

	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

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
