package cn.net.susan.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeDeliveryAddressEntity extends BaseEntity {


	/**
	 * 订单ID
	 */
	private Long tradeId;

	/**
	 * 订单编码
	 */
	private String code;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户名称
	 */
	private String userName;

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
	private String province;

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
