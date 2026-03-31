package cn.net.susan.mapper.common;

import cn.net.susan.entity.common.CommonAreaConditionEntity;
import cn.net.susan.entity.common.CommonAreaEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CommonAreaMapper extends BaseMapper<CommonAreaEntity, CommonAreaConditionEntity> {
	/**
     * 查询地区信息
     *
     * @param id 地区ID
     * @return 地区信息
     */
	CommonAreaEntity findById(Long id);

	/**
     * 添加地区
     *
     * @param commonAreaEntity 地区信息
     * @return 结果
     */
	int insert(CommonAreaEntity commonAreaEntity);

	/**
     * 修改地区
     *
     * @param commonAreaEntity 地区信息
     * @return 结果
     */
	int update(CommonAreaEntity commonAreaEntity);

    /**
     * 批量删除地区
     *
     * @param ids id集合
     * @param entity 地区实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CommonAreaEntity entity);

    /**
     * 批量查询地区信息
     *
     * @param ids ID集合
     * @return 地区信息
    */
    List<CommonAreaEntity> findByIds(List<Long> ids);
}
