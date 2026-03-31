package cn.net.susan.mapper.mall;

import cn.net.susan.entity.mall.UnitConditionEntity;
import cn.net.susan.entity.mall.UnitEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface UnitMapper extends BaseMapper<UnitEntity, UnitConditionEntity> {
	/**
     * 查询单位信息
     *
     * @param id 单位ID
     * @return 单位信息
     */
	UnitEntity findById(Long id);

	/**
     * 添加单位
     *
     * @param unitEntity 单位信息
     * @return 结果
     */
	int insert(UnitEntity unitEntity);

	/**
     * 修改单位
     *
     * @param unitEntity 单位信息
     * @return 结果
     */
	int update(UnitEntity unitEntity);

	/**
     * 批量删除单位
     *
     * @param ids id集合
     * @param entity 单位实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") UnitEntity entity);

	/**
     * 批量查询单位信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<UnitEntity> findByIds(List<Long> ids);
}
