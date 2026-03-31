package cn.net.susan.mapper.sys;

import cn.net.susan.entity.sys.DictConditionEntity;
import cn.net.susan.entity.sys.DictEntity;
import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DictMapper extends BaseMapper<DictEntity, DictConditionEntity> {
    /**
     * 查询数据字典信息
     *
     * @param id 数据字典ID
     * @return 数据字典信息
     */
    DictEntity findById(Long id);

    /**
     * 添加数据字典
     *
     * @param dictEntity 数据字典信息
     * @return 结果
     */
    int insert(DictEntity dictEntity);

    /**
     * 修改数据字典
     *
     * @param dictEntity 数据字典信息
     * @return 结果
     */
    int update(DictEntity dictEntity);

    /**
     * 删除数据字典
     *
     * @param id 数据字典ID
     * @return 结果
     */
    int deleteById(Long id);


    /**
     * 批量查询数据字典信息
     *
     * @param ids ID集合
     * @return 数据字典信息
     */
    List<DictEntity> findByIds(List<Long> ids);

    /**
     * 删除数据字典
     *
     * @param ids        id集合
     * @param dictEntity 数据字段实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("dictEntity") DictEntity dictEntity);

}
