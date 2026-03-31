package cn.net.susan.entity.mall;

import cn.net.susan.entity.BaseEntity;
import lombok.Data;


@Data
public class BaseProductEntity extends BaseEntity {

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;
}
