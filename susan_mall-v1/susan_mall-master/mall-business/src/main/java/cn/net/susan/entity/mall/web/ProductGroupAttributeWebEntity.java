package cn.net.susan.entity.mall.web;

import lombok.Data;

import java.util.List;


@Data
public class ProductGroupAttributeWebEntity {

    /**
     * 属性
     */
    private Long id;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性值集合
     */
    private List<ProductGroupAttributeValueWebEntity> valueList;

}
