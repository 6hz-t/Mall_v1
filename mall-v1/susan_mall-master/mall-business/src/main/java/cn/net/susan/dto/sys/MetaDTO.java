package cn.net.susan.dto.sys;

import lombok.Data;


@Data
public class MetaDTO {

    /**
     * icon
     */
    private String icon;

    /**
     * 是否不缓存
     */
    private Boolean noCache;

    /**
     * 菜单标题
     */
    private String title;
}
