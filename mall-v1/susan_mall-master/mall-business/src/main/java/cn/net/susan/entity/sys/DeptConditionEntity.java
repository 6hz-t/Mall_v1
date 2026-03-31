package cn.net.susan.entity.sys;

import cn.net.susan.entity.RequestConditionEntity;
import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("部门查询条件实体")
@Data
public class DeptConditionEntity extends RequestConditionEntity {


    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * ids
     */
    @ApiModelProperty("ids")
    private List<Long> idList;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 上级部门
     */
    @ApiModelProperty("上级部门")
    private Long pid;

    /**
     * 有效状态 1:有效 0:无效
     */
    @ApiModelProperty("有效状态 1:有效 0:无效")
    private Integer validStatus;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private Long createUserId;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty("修改人ID")
    private Long updateUserId;

    /**
     * 修改人名称
     */
    @ApiModelProperty("修改人名称")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 是否删除 1：已删除 0：未删除
     */
    @ApiModelProperty("是否删除 1：已删除 0：未删除")
    private Integer isDel;

    /**
     * 是否查询树
     */
    @ApiModelProperty("是否查询树")
    private Boolean queryTree;
}
