package cn.net.susan.entity.common;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("任务实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonTaskEntity extends BaseEntity {


    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String name;

    /**
     * 下载文件地址
     */
    @ApiModelProperty("下载文件地址")
    private String fileUrl;

    /**
     * 任务类型 1：通用excel导出
     */
    @ApiModelProperty("任务类型 1：通用excel导出 2：发邮件")
    private Integer type;

    /**
     * 执行状态 0：待执行 1：执行中 2：成功 3：失败
     */
    @ApiModelProperty("执行状态 0：待执行 1：执行中 2：成功 3：失败")
    private Integer status;

    /**
     * 失败次数
     */
    @ApiModelProperty("失败次数")
    private Integer failureCount;

    /**
     * 业务类型
     * 任务类型时通用excel导出时
     * 1：菜单 2：部门 3：角色 4：用户
     * <p>
     * 任务类型时发邮件时
     * 1：异地登录
     */
    @ApiModelProperty("业务类型 1：菜单 2：部门 3：角色 4：用户")
    private Integer bizType;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    private String requestParam;
}
