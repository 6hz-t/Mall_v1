package cn.net.susan.entity.sys;

import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel("用户角色关联查询条件实体")
@Data
public class UserRoleConditionEntity extends RequestPageEntity {


    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户ID集合
     */
    @ApiModelProperty("用户ID集合")
    private List<Long> userIdList;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Long roleId;
}
