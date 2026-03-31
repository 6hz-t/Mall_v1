package cn.net.susan.entity.sys;

import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("用户头像查询条件实体")
@Data
public class UserAvatarConditionEntity extends RequestPageEntity {

    /**
     * 系统编号
     */
    @ApiModelProperty("")
    private Long id;


    /**
     * 系统编号集合
     */
    private List<Long> idList;

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 路径
     */
    @ApiModelProperty("路径")
    private String path;

    /**
     * 大小
     */
    @ApiModelProperty("大小")
    private String fileSize;

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
}
