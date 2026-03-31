package cn.net.susan.entity.common;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("图片分组实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonPhotoGroupEntity extends BaseEntity {


	/**
	 * 分组名称
	 */
	@ApiModelProperty("分组名称")
	private String name;
}
