package cn.net.susan.entity.mall;

import cn.net.susan.entity.RequestConditionEntity;
import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("分类查询条件实体")
@Data
public class CategoryConditionEntity extends RequestConditionEntity {


    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * ID集合
     */
    @ApiModelProperty("ID集合")
    private List<Long> idList;

    /**
     * 父分类ID
     */
    @ApiModelProperty("父分类ID")
    private Long parentId;

    /**
     * 定时任务名称
     */
    @ApiModelProperty("定时任务名称")
    private String name;

    /**
     * 层级
     */
    @ApiModelProperty("层级")
    private Integer level;

    /**
     * 是否叶子节点
     */
    @ApiModelProperty("是否叶子节点")
    private Integer isLeaf;

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
     * 是否查询整课分类树
     */
    @ApiModelProperty("是否查询整课分类树")
    private Boolean queryTree;
}
