package cn.net.susan.mapper.sys;

import cn.net.susan.entity.sys.DictDetailConditionEntity;
import cn.net.susan.entity.sys.DictDetailEntity;
import cn.net.susan.entity.sys.DictEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DictDetailMapper {
    /**
     * 查询数据字典详情信息
     *
     * @param id 部门ID
     * @return 部门信息
     */
    DictDetailEntity findById(Long id);

    /**
     * 根据条件查询数据字典详情列表
     *
     * @param dictDetailConditionEntity 数据字典详情信息
     * @return 部门集合
     */
    List<DictDetailEntity> searchByCondition(DictDetailConditionEntity dictDetailConditionEntity);

    /**
     * 根据条件查询数据字典详情数量
     *
     * @param dictDetailConditionEntity 数据字典详情信息
     * @return 部门集合
     */
    int searchCount(DictDetailConditionEntity dictDetailConditionEntity);

    /**
     * 添加数据字典详情
     *
     * @param dictDetailEntity 数据字典详情信息
     * @return 结果
     */
    int insert(DictDetailEntity dictDetailEntity);

    /**
     * 修改数据字典详情
     *
     * @param dictDetailEntity 数据字典详情信息
     * @return 结果
     */
    int update(DictDetailEntity dictDetailEntity);

    /**
     * 删除数据字典详情
     *
     * @param id 数据字典详情ID
     * @return 结果
     */
    int deleteById(Long id);


    /**
     * 批量查询数据字典详情信息
     *
     * @param ids ID集合
     * @return 数据字段信息
     */
    List<DictDetailEntity> findByIds(List<Long> ids);

    /**
     * 删除数据字典详情
     *
     * @param ids              id集合
     * @param dictDetailEntity 数据字段实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("dictDetailEntity") DictDetailEntity dictDetailEntity);
}
