package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ProductTypeEnum {

    /**
     * 热门商品
     */
    HOT(1, "热门商品"),

    /**
     * 新品推荐
     */
    NEW(2, "新品推荐"),

    /**
     * 秒杀商品
     */
    SECKILL(3, "秒杀商品");

    private Integer value;

    private String desc;
}
