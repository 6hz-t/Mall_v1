package cn.net.susan.mapper.common;

import cn.net.susan.entity.common.CommonSmsRecordConditionEntity;
import cn.net.susan.entity.common.CommonSmsRecordEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CommonSmsRecordMapper extends BaseMapper<CommonSmsRecordEntity, CommonSmsRecordConditionEntity> {
	/**
     * 查询短信发送记录信息
     *
     * @param id 短信发送记录ID
     * @return 短信发送记录信息
     */
	CommonSmsRecordEntity findById(Long id);

	/**
     * 添加短信发送记录
     *
     * @param commonSmsRecordEntity 短信发送记录信息
     * @return 结果
     */
	int insert(CommonSmsRecordEntity commonSmsRecordEntity);

	/**
     * 修改短信发送记录
     *
     * @param commonSmsRecordEntity 短信发送记录信息
     * @return 结果
     */
	int update(CommonSmsRecordEntity commonSmsRecordEntity);

    /**
     * 批量删除短信发送记录
     *
     * @param ids id集合
     * @param entity 短信发送记录实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CommonSmsRecordEntity entity);

    /**
     * 批量查询短信发送记录信息
     *
     * @param ids ID集合
     * @return 短信发送记录信息
    */
    List<CommonSmsRecordEntity> findByIds(List<Long> ids);
}
