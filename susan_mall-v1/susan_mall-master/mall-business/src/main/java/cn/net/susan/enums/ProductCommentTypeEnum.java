package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ProductCommentTypeEnum {

    /**
     * 好评
     */
    POSITIVE(1, "好评"),

    /**
     * 中评
     */
    MODERATE(2, "中评"),

    /**
     * 差评
     */
    NEGATIVE(3, "差评");

    private Integer value;

    private String desc;
}
