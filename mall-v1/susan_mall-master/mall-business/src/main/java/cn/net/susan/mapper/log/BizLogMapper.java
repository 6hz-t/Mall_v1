package cn.net.susan.mapper.log;

import cn.net.susan.entity.log.BizLogConditionEntity;
import cn.net.susan.entity.log.BizLogEntity;
import cn.net.susan.mapper.BaseMapper;


public interface BizLogMapper  extends BaseMapper<BizLogEntity, BizLogConditionEntity> {
	/**
     * 查询业务日志信息
     *
     * @param id 业务日志ID
     * @return 业务日志信息
     */
	BizLogEntity findById(Long id);

	/**
     * 添加业务日志
     *
     * @param bizLogEntity 业务日志信息
     * @return 结果
     */
	int insert(BizLogEntity bizLogEntity);

	/**
     * 修改业务日志
     *
     * @param bizLogEntity 业务日志信息
     * @return 结果
     */
	int update(BizLogEntity bizLogEntity);

	/**
     * 删除业务日志
     *
     * @param id 业务日志ID
     * @return 结果
     */
	int deleteById(Long id);

}
