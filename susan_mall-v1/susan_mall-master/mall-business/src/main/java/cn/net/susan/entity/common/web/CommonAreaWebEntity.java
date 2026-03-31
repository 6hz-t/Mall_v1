package cn.net.susan.entity.common.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonAreaWebEntity {
    /**
     * 系统ID
     */
    private Long id;

    /**
     * 上一级ID
     */
    private Long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 全称
     */
    private String fullName;

    /**
     * 行政编码
     */
    private String code;

    /**
     * 级别
     */
    private Integer level;
}
