package cn.net.susan.entity.sys;

import cn.net.susan.entity.RequestConditionEntity;
import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("数据字典查询条件实体")
@Data
public class DictConditionEntity extends RequestConditionEntity {


    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 父字段ID
     */
    @ApiModelProperty("父字段ID")
    private Long parentId;

    /**
     * 字典名称
     */
    @ApiModelProperty("字典名称")
    private String dictName;

    /**
     * 字典描述
     */
    @ApiModelProperty("字典描述")
    private String dictDescription;

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
     * 查询条件
     */
    @ApiModelProperty("查询条件")
    private String blurry;
}
