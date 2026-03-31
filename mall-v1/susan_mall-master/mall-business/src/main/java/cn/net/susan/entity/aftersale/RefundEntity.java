package cn.net.susan.entity.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefundEntity extends BaseEntity {


	/**
	 * 订单ID
	 */
	private Long tradeId;

	/**
	 * 订单编码
	 */
	private String tradeCode;

	/**
	 * 商品ID
	 */
	private Long productId;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 规格
	 */
	private String model;

	/**
	 * 数量
	 */
	private Integer quantity;

	/**
	 * 封面图片url
	 */
	private String coverUrl;

	/**
	 * 总金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;

	/**
	 * 退货类型 10：退货退款 20：换货
	 */
	private Integer refundType;

	/**
	 * 审核状态 10：待审核 20：已同意 30：已拒绝
	 */
	private Integer auditStatus;

	/**
	 * 退货状态 10：进行中 20：已拒绝 30：已完成 40：已取消
	 */
	private Integer refundStatus;

	/**
	 * 申请原因
	 */
	private String content;

	/**
	 * 拒绝原因
	 */
	private String rejectedReason;
}
