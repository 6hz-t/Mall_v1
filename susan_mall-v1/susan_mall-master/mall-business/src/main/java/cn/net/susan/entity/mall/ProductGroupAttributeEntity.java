package cn.net.susan.entity.mall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductGroupAttributeEntity extends BaseEntity {


	/**
	 * 商品组ID
	 */
	private Long productGroupId;

	/**
	 * 属性ID
	 */
	private Long attributeId;

	/**
	 * 属性值ID
	 */
	private Long attributeValueId;
}
