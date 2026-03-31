package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.IndexProductConditionEntity;
import cn.net.susan.entity.mall.IndexProductEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface IndexProductMapper extends BaseMapper<IndexProductEntity, IndexProductConditionEntity> {
	/**
     * 查询首页商品信息
     *
     * @param id 首页商品ID
     * @return 首页商品信息
     */
	IndexProductEntity findById(Long id);

	/**
     * 添加首页商品
     *
     * @param indexProductEntity 首页商品信息
     * @return 结果
     */
	int insert(IndexProductEntity indexProductEntity);

	/**
     * 修改首页商品
     *
     * @param indexProductEntity 首页商品信息
     * @return 结果
     */
	int update(IndexProductEntity indexProductEntity);

	/**
     * 批量删除首页商品
     *
     * @param ids id集合
     * @param entity 首页商品实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") IndexProductEntity entity);

	/**
     * 批量查询首页商品信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<IndexProductEntity> findByIds(List<Long> ids);
}
