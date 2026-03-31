package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.ProductGroupConditionEntity;
import cn.net.susan.entity.mall.ProductGroupEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface ProductGroupMapper extends BaseMapper<ProductGroupEntity, ProductGroupConditionEntity> {
    /**
     * 查询商品组信息
     *
     * @param id 商品组ID
     * @return 商品组信息
     */
    ProductGroupEntity findById(Long id);

    /**
     * 添加商品组
     *
     * @param productGroupEntity 商品组信息
     * @return 结果
     */
    int insert(ProductGroupEntity productGroupEntity);

    /**
     * 修改商品组
     *
     * @param productGroupEntity 商品组信息
     * @return 结果
     */
    int update(ProductGroupEntity productGroupEntity);

    /**
     * 批量删除商品组
     *
     * @param ids    id集合
     * @param entity 商品组实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductGroupEntity entity);

    /**
     * 批量查询商品组信息
     *
     * @param ids ID集合
     * @return 商品组信息
     */
    List<ProductGroupEntity> findByIds(List<Long> ids);

    /**
     * 批量添加商品组
     *
     * @param list 商品组集合
     * @return 结果
     */
    int batchInsert(List<ProductGroupEntity> list);
}
