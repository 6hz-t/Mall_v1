package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.CategoryConditionEntity;
import cn.net.susan.entity.mall.CategoryEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CategoryMapper extends BaseMapper<CategoryEntity, CategoryConditionEntity> {
	/**
     * 查询分类信息
     *
     * @param id 分类ID
     * @return 分类信息
     */
	CategoryEntity findById(Long id);

	/**
     * 添加分类
     *
     * @param categoryEntity 分类信息
     * @return 结果
     */
	int insert(CategoryEntity categoryEntity);

	/**
     * 修改分类
     *
     * @param categoryEntity 分类信息
     * @return 结果
     */
	int update(CategoryEntity categoryEntity);

	/**
     * 批量删除分类
     *
     * @param ids id集合
     * @param entity 分类实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CategoryEntity entity);

	/**
     * 批量查询分类信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<CategoryEntity> findByIds(List<Long> ids);
}
