package cn.net.susan.entity.mall;

import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("商品属性查询条件实体")
@Data
public class ProductAttributeConditionEntity extends RequestPageEntity {


	/**
	 *  ID
     */
	@ApiModelProperty("ID")
	private Long id;

	/**
	 *  商品ID
     */
	@ApiModelProperty("商品ID")
	private Long productId;

	/**
	 * 商品ID集合
	 */
	private List<Long> productIdList;

	/**
	 *  属性ID
     */
	@ApiModelProperty("属性ID")
	private Long attributeId;

	/**
	 *  属性值ID
     */
	@ApiModelProperty("属性值ID")
	private Long attributeValueId;

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
