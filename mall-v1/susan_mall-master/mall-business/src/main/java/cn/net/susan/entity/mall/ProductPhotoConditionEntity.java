package cn.net.susan.entity.mall;

import cn.net.susan.entity.RequestPageEntity;
import lombok.Data;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("商品图片查询条件实体")
@Data
public class ProductPhotoConditionEntity extends RequestPageEntity {


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
	 *  商品ID集合
	 */
	@ApiModelProperty("商品ID集合")
	private List<Long> productIdList;

	/**
	 *  图片名称
     */
	@ApiModelProperty("图片名称")
	private String name;

	/**
	 *  图片url
     */
	@ApiModelProperty("图片url")
	private String url;

	/**
	 *  排序
     */
	@ApiModelProperty("排序")
	private Integer sort;

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

	/**
	 * 图片类型 1：封面 2：轮播图
	 */
	@ApiModelProperty("图片类型")
	private Integer type;
}
