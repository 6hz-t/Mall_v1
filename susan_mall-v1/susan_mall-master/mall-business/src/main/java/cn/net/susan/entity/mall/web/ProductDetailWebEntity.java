package cn.net.susan.entity.mall.web;

import cn.net.susan.entity.mall.ProductEntity;
import lombok.Data;

import java.util.List;



@Data
public class ProductDetailWebEntity extends ProductEntity {

    /**
     * 是否已收藏商品
     */
    private Boolean isFavorites;

    /**
     * 购物车商品数量
     */
    private int totalCartNum;

    /**
     * 商品评价统计
     */
    private ProductCommentStatisticWebEntity productCommentStatistic;

    /**
     * 属性集合
     */
    private List<ProductGroupAttributeWebEntity> groupAttributeList;

    /**
     * 同一组的商品列表
     */
    private List<ProductEntity> groupProductList;
}
