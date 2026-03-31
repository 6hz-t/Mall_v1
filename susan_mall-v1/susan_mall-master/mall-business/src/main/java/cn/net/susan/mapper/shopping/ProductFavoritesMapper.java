package cn.net.susan.mapper.shopping;

import cn.net.susan.entity.shopping.ProductFavoritesConditionEntity;
import cn.net.susan.entity.shopping.ProductFavoritesEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface ProductFavoritesMapper extends BaseMapper<ProductFavoritesEntity, ProductFavoritesConditionEntity> {
	/**
     * 查询商品收藏信息
     *
     * @param id 商品收藏ID
     * @return 商品收藏信息
     */
	ProductFavoritesEntity findById(Long id);

	/**
     * 添加商品收藏
     *
     * @param productFavoritesEntity 商品收藏信息
     * @return 结果
     */
	int insert(ProductFavoritesEntity productFavoritesEntity);

	/**
     * 修改商品收藏
     *
     * @param productFavoritesEntity 商品收藏信息
     * @return 结果
     */
	int update(ProductFavoritesEntity productFavoritesEntity);

    /**
     * 批量删除商品收藏
     *
     * @param ids id集合
     * @param entity 商品收藏实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductFavoritesEntity entity);

    /**
     * 批量查询商品收藏信息
     *
     * @param ids ID集合
     * @return 商品收藏信息
    */
    List<ProductFavoritesEntity> findByIds(List<Long> ids);
}
