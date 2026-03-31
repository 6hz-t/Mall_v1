package cn.net.susan.entity.mall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductGroupEntity extends BaseProductEntity {

    /**
     * 商品组名称
     */
    private String name;

    /**
     * 规格
     */
    private String model;

    /**
     * hash值
     */
    private String hash;

    /**
     * 是否新创建的商品组
     */
    private Boolean isNew;

    /**
     * 逻辑删除ID，默认是0，表示未删除
     */
    private Long delId;
}
