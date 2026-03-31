package cn.net.susan.mapper.common;

import cn.net.susan.entity.common.CommonSensitiveWordConditionEntity;
import cn.net.susan.entity.common.CommonSensitiveWordEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CommonSensitiveWordMapper extends BaseMapper<CommonSensitiveWordEntity, CommonSensitiveWordConditionEntity> {
    /**
     * 查询敏感词信息
     *
     * @param id 敏感词ID
     * @return 敏感词信息
     */
    CommonSensitiveWordEntity findById(Long id);

    /**
     * 添加敏感词
     *
     * @param commonSensitiveWordEntity 敏感词信息
     * @return 结果
     */
    int insert(CommonSensitiveWordEntity commonSensitiveWordEntity);

    /**
     * 批量添加敏感词
     *
     * @param list 敏感词信息
     * @return 结果
     */
    int batchInsert(List<CommonSensitiveWordEntity> list);

    /**
     * 修改敏感词
     *
     * @param commonSensitiveWordEntity 敏感词信息
     * @return 结果
     */
    int update(CommonSensitiveWordEntity commonSensitiveWordEntity);

    /**
     * 批量删除敏感词
     *
     * @param ids    id集合
     * @param entity 敏感词实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CommonSensitiveWordEntity entity);

    /**
     * 批量查询敏感词信息
     *
     * @param ids ID集合
     * @return 部门信息
     */
    List<CommonSensitiveWordEntity> findByIds(List<Long> ids);
}
