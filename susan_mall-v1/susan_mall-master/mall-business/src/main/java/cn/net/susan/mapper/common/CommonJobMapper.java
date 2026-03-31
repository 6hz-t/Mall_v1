package cn.net.susan.mapper.common;

import cn.net.susan.entity.common.CommonJobConditionEntity;
import cn.net.susan.entity.common.CommonJobEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CommonJobMapper extends BaseMapper<CommonJobEntity, CommonJobConditionEntity> {
	/**
     * 查询定时任务信息
     *
     * @param id 定时任务ID
     * @return 定时任务信息
     */
	CommonJobEntity findById(Long id);

	/**
     * 添加定时任务
     *
     * @param commonJobEntity 定时任务信息
     * @return 结果
     */
	int insert(CommonJobEntity commonJobEntity);

	/**
     * 修改定时任务
     *
     * @param commonJobEntity 定时任务信息
     * @return 结果
     */
	int update(CommonJobEntity commonJobEntity);

	/**
     * 批量删除定时任务
     *
     * @param ids id集合
     * @param entity 定时任务实体
     * @return 结果
     */
	int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CommonJobEntity entity);

	/**
     * 批量查询定时任务信息
     *
     * @param ids ID集合
     * @return 部门信息
    */
	List<CommonJobEntity> findByIds(List<Long> ids);
}
