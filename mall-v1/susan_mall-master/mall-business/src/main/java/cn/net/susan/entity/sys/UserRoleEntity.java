package cn.net.susan.entity.sys;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("用户角色关联实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRoleEntity extends BaseEntity {
	

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户ID")
	private Long userId;

	/**
	 * 角色ID
	 */
	@ApiModelProperty("角色ID")
	private Long roleId;
}
