package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ValidStatusEnum {

    /**
     * 有效
     */
    VALID(1, "有效"),

    /**
     * 无效
     */
    INVALID(0, "无效");

    private Integer value;

    private String desc;
}
