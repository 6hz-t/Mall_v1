package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.ProductAttributeConditionEntity;
import cn.net.susan.entity.mall.ProductAttributeEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface ProductAttributeMapper extends BaseMapper<ProductAttributeEntity, ProductAttributeConditionEntity> {
    /**
     * 查询商品属性信息
     *
     * @param id 商品属性ID
     * @return 商品属性信息
     */
    ProductAttributeEntity findById(Long id);

    /**
     * 添加商品属性
     *
     * @param productAttributeEntity 商品属性信息
     * @return 结果
     */
    int insert(ProductAttributeEntity productAttributeEntity);

    /**
     * 批量添加商品属性
     *
     * @param productAttributeEntities 商品属性信息
     * @return 结果
     */
    int batchInsert(List<ProductAttributeEntity> productAttributeEntities);

    /**
     * 修改商品属性
     *
     * @param productAttributeEntity 商品属性信息
     * @return 结果
     */
    int update(ProductAttributeEntity productAttributeEntity);

    /**
     * 批量删除商品属性
     *
     * @param ids    id集合
     * @param entity 商品属性实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductAttributeEntity entity);

    /**
     * 批量查询商品属性信息
     *
     * @param ids ID集合
     * @return 部门信息
     */
    List<ProductAttributeEntity> findByIds(List<Long> ids);
}
