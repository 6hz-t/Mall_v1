package cn.net.susan.mapper.common;

import cn.net.susan.entity.common.CommonNotifyConditionEntity;
import cn.net.susan.entity.common.CommonNotifyEntity;
import cn.net.susan.mapper.BaseMapper;


public interface CommonNotifyMapper  extends BaseMapper<CommonNotifyEntity, CommonNotifyConditionEntity> {
	/**
     * 查询通知信息
     *
     * @param id 通知ID
     * @return 通知信息
     */
	CommonNotifyEntity findById(Long id);

	/**
     * 添加通知
     *
     * @param commonNotifyEntity 通知信息
     * @return 结果
     */
	int insert(CommonNotifyEntity commonNotifyEntity);

	/**
     * 修改通知
     *
     * @param commonNotifyEntity 通知信息
     * @return 结果
     */
	int update(CommonNotifyEntity commonNotifyEntity);

	/**
     * 删除通知
     *
     * @param id 通知ID
     * @return 结果
     */
	int deleteById(Long id);

}
