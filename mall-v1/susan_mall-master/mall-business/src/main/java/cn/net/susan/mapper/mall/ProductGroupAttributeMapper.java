package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.ProductGroupAttributeConditionEntity;
import cn.net.susan.entity.mall.ProductGroupAttributeEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface ProductGroupAttributeMapper extends BaseMapper<ProductGroupAttributeEntity, ProductGroupAttributeConditionEntity> {
    /**
     * 查询商品组属性信息
     *
     * @param id 商品组属性ID
     * @return 商品组属性信息
     */
    ProductGroupAttributeEntity findById(Long id);

    /**
     * 添加商品组属性
     *
     * @param productGroupAttributeEntity 商品组属性信息
     * @return 结果
     */
    int insert(ProductGroupAttributeEntity productGroupAttributeEntity);

    /**
     * 修改商品组属性
     *
     * @param productGroupAttributeEntity 商品组属性信息
     * @return 结果
     */
    int update(ProductGroupAttributeEntity productGroupAttributeEntity);

    /**
     * 批量删除商品组属性
     *
     * @param ids    id集合
     * @param entity 商品组属性实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductGroupAttributeEntity entity);

    /**
     * 批量查询商品组属性信息
     *
     * @param ids ID集合
     * @return 商品组属性信息
     */
    List<ProductGroupAttributeEntity> findByIds(List<Long> ids);

    /**
     * 批量添加商品组属性
     *
     * @param list 商品组属性
     * @return 结果
     */
    int batchInsert(List<ProductGroupAttributeEntity> list);
}
