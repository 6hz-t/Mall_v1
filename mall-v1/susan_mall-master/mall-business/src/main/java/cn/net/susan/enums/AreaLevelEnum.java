package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum AreaLevelEnum {

    /**
     * 省份
     */
    PROVINCE(1, "省份"),

    /**
     * 城市
     */
    CITY(2, "城市"),

    /**
     * 区县
     */
    DISTRICT(3, "区县"),

    /**
     * 乡镇
     */
    TOWN(4, "乡镇");

    /**
     * 枚举值
     */
    private Integer value;


    /**
     * 枚举描述
     */
    private String desc;
}
