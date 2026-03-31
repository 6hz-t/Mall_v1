package cn.net.susan.entity.aftersale.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefundWebEntity {

    /**
     * 系统ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long tradeId;

    /**
     * 订单编码
     */
    @NotEmpty(message = "订单编码不能为空")
    private String tradeCode;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 订单明细ID
     */
    @NotNull(message = "订单明细ID不能为空")
    private Long tradeItemId;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 凭证
     */
    private List<String> images;

}
