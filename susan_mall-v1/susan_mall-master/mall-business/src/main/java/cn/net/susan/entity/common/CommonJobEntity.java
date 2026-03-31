package cn.net.susan.entity.common;

import cn.net.susan.entity.BaseEntity;
import cn.net.susan.enums.CommonJobOperateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("定时任务实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonJobEntity extends BaseEntity {


    /**
     * 定时任务名称
     */
    @ApiModelProperty("定时任务名称")
    private String jobName;

    /**
     * 暂停状态 0：未暂停 1：已暂停
     */
    @ApiModelProperty("暂停状态 0：未暂停 1：已暂停")
    private Boolean pauseStatus;

    /**
     * bean名称
     */
    @ApiModelProperty("bean名称")
    private String beanName;

    /**
     * 方法名称
     */
    @ApiModelProperty("方法名称")
    private String methodName;

    /**
     * cron 表达式
     */
    @ApiModelProperty("cron 表达式")
    private String cronExpression;

    /**
     * 参数
     */
    @ApiModelProperty("参数")
    private String params;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private CommonJobOperateTypeEnum operateTypeEnum;
}
