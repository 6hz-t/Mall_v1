package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.IndexCarouselImageConditionEntity;
import cn.net.susan.entity.mall.IndexCarouselImageEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface IndexCarouselImageMapper extends BaseMapper<IndexCarouselImageEntity, IndexCarouselImageConditionEntity> {
	/**
     * 查询首页轮播图信息
     *
     * @param id 首页轮播图ID
     * @return 首页轮播图信息
     */
	IndexCarouselImageEntity findById(Long id);

	/**
     * 添加首页轮播图
     *
     * @param indexCarouselImageEntity 首页轮播图信息
     * @return 结果
     */
	int insert(IndexCarouselImageEntity indexCarouselImageEntity);

	/**
     * 修改首页轮播图
     *
     * @param indexCarouselImageEntity 首页轮播图信息
     * @return 结果
     */
	int update(IndexCarouselImageEntity indexCarouselImageEntity);

	/**
     * 批量删除首页轮播图
     *
     * @param ids id集合
     * @param entity 首页轮播图实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") IndexCarouselImageEntity entity);

	/**
     * 批量查询首页轮播图信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<IndexCarouselImageEntity> findByIds(List<Long> ids);
}
