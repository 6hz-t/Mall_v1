package cn.net.susan.entity.mall;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("首页轮播图实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndexCarouselImageEntity extends BaseEntity {


	/**
	 * 图片url
	 */
	@ApiModelProperty("图片url")
	private String url;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer sort;
}
