package cn.net.susan.entity.common;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("图片实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonPhotoEntity extends BaseEntity {


	/**
	 * 图片分组ID
	 */
	@ApiModelProperty("图片分组ID")
	private Long photoGroupId;

	/**
	 * 图片名称
	 */
	@ApiModelProperty("图片名称")
	private String name;

	/**
	 * 图片url
	 */
	@ApiModelProperty("图片url")
	private String url;
}
