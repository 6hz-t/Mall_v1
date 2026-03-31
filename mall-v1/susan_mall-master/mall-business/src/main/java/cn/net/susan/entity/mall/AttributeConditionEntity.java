package cn.net.susan.entity.mall;

import cn.net.susan.entity.RequestConditionEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@ApiModel("属性查询条件实体")
@Data
public class AttributeConditionEntity extends RequestConditionEntity {


	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 * ID集合
	 */
	@ApiModelProperty("ID集合")
	private List<Long> idList;

	/**
	 *  属性名称
     */
	@ApiModelProperty("属性名称")
	private String name;

	/**
	 *  创建人ID
     */
	@ApiModelProperty("创建人ID")
	private Long createUserId;

	/**
	 *  创建人名称
     */
	@ApiModelProperty("创建人名称")
	private String createUserName;

	/**
	 *  创建日期
     */
	@ApiModelProperty("创建日期")
	private Date createTime;

	/**
	 *  修改人ID
     */
	@ApiModelProperty("修改人ID")
	private Long updateUserId;

	/**
	 *  修改人名称
     */
	@ApiModelProperty("修改人名称")
	private String updateUserName;

	/**
	 *  修改时间
     */
	@ApiModelProperty("修改时间")
	private Date updateTime;

	/**
	 *  是否删除 1：已删除 0：未删除
     */
	@ApiModelProperty("是否删除 1：已删除 0：未删除")
	private Integer isDel;
}
