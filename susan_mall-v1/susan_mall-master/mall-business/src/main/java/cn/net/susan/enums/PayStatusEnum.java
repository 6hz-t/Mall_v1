package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum PayStatusEnum {

    /**
     * 待支付
     */
    WAIT_PAY(1, "待支付"),

    /**
     * 已支付
     */
    PAYMENT(2, "已支付"),

    /**
     * 已退款
     */
    REFUND(3, "已退款"),

    /**
     * 失败
     */
    FAILURE(4, "失败");

    private Integer value;

    private String desc;
}
