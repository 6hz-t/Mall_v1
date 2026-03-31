package cn.net.susan.mapper.shopping;

import cn.net.susan.entity.shopping.ShoppingCartConditionEntity;
import cn.net.susan.entity.shopping.ShoppingCartEntity;
import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ShoppingCartMapper extends BaseMapper<ShoppingCartEntity, ShoppingCartConditionEntity>  {
	/**
     * 查询购物车信息
     *
     * @param id 购物车ID
     * @return 购物车信息
     */
	ShoppingCartEntity findById(Long id);

	/**
     * 添加购物车
     *
     * @param shoppingCartEntity 购物车信息
     * @return 结果
     */
	int insert(ShoppingCartEntity shoppingCartEntity);

	/**
     * 修改购物车
     *
     * @param shoppingCartEntity 购物车信息
     * @return 结果
     */
	int update(ShoppingCartEntity shoppingCartEntity);

    /**
     * 批量删除购物车
     *
     * @param ids id集合
     * @param entity 购物车实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ShoppingCartEntity entity);

    /**
     * 批量查询购物车信息
     *
     * @param ids ID集合
     * @return 购物车信息
    */
    List<ShoppingCartEntity> findByIds(List<Long> ids);
}
