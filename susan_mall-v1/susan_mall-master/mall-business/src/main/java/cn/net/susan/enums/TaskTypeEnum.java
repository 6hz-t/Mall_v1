package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    EXPORT_EXCEL(1, "通用excel数据导出"),

    SEND_EMAIL(2, "发送邮件");

    /**
     * 枚举值
     */
    private Integer value;


    /**
     * 枚举描述
     */
    private String desc;
}
