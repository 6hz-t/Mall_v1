package cn.net.susan.mapper.common;

import cn.net.susan.entity.common.CommonTaskConditionEntity;
import cn.net.susan.entity.common.CommonTaskEntity;
import cn.net.susan.mapper.BaseMapper;


public interface CommonTaskMapper  extends BaseMapper<CommonTaskEntity, CommonTaskConditionEntity> {
	/**
     * 查询任务信息
     *
     * @param id 任务ID
     * @return 任务信息
     */
	CommonTaskEntity findById(Long id);

	/**
     * 添加任务
     *
     * @param commonTaskEntity 任务信息
     * @return 结果
     */
	int insert(CommonTaskEntity commonTaskEntity);

	/**
     * 修改任务
     *
     * @param commonTaskEntity 任务信息
     * @return 结果
     */
	int update(CommonTaskEntity commonTaskEntity);

	/**
     * 删除任务
     *
     * @param id 任务ID
     * @return 结果
     */
	int deleteById(Long id);

}
