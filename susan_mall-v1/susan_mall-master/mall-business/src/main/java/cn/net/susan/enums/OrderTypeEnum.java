package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    NORMAL_PRODUCT(1, "普通商品订单"),

    SECKILL_PRODUCT(2, "秒杀商品订单");

    /**
     * 枚举值
     */
    private Integer value;


    /**
     * 枚举描述
     */
    private String desc;
}
