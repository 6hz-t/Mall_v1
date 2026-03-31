package cn.net.susan.entity.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefundPhotoEntity extends BaseEntity {


	/**
	 * 退货单ID
	 */
	private Long refundId;

	/**
	 * 图片名称
	 */
	private String name;

	/**
	 * 图片url
	 */
	private String url;
}
