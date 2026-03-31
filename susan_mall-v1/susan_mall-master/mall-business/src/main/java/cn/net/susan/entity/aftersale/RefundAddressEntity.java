package cn.net.susan.entity.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefundAddressEntity extends BaseEntity {


	/**
	 * 退货单ID
	 */
	private Long refundId;

	/**
	 * 收货人姓名
	 */
	private String receiverName;

	/**
	 * 收货人手机号
	 */
	private String receiverPhone;

	/**
	 * 省份
	 */
	private String receiverProvince;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 区县
	 */
	private String district;

	/**
	 * 详细地址
	 */
	private String detailAddress;

	/**
	 * 邮编
	 */
	private String postCode;
}
