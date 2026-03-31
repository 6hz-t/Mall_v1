package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.ProductDetailConditionEntity;
import cn.net.susan.entity.mongo.ProductDetailEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface ProductDetailMapper extends BaseMapper<ProductDetailEntity, ProductDetailConditionEntity> {
    /**
     * 查询商品详情信息
     *
     * @param id 商品详情ID
     * @return 商品详情信息
     */
    ProductDetailEntity findById(Long id);

    /**
     * 添加商品详情
     *
     * @param productDetailEntity 商品详情信息
     * @return 结果
     */
    int insert(ProductDetailEntity productDetailEntity);

    /**
     * 批量添加商品详情
     *
     * @param productDetailEntityList 商品详情信息
     * @return 结果
     */
    int batchInsert(List<ProductDetailEntity> productDetailEntityList);

    /**
     * 修改商品详情
     *
     * @param productDetailEntity 商品详情信息
     * @return 结果
     */
    int update(ProductDetailEntity productDetailEntity);

    /**
     * 批量删除商品详情
     *
     * @param ids    id集合
     * @param entity 商品详情实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductDetailEntity entity);

    /**
     * 批量查询商品详情信息
     *
     * @param ids ID集合
     * @return 部门信息
     */
    List<ProductDetailEntity> findByIds(List<Long> ids);
}
