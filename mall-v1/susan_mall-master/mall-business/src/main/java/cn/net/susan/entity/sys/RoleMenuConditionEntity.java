package cn.net.susan.entity.sys;

import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel("角色菜单关联查询条件实体")
@Data
public class RoleMenuConditionEntity extends RequestPageEntity {


    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Long roleId;

    /**
     * 菜单ID
     */
    @ApiModelProperty("菜单ID")
    private Long menuId;

    /**
     * 角色ID集合
     */
    private List<Long> roleIdList;
}
