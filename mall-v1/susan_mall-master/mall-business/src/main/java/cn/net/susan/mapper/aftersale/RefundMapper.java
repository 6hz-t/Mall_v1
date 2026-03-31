package cn.net.susan.mapper.aftersale;

import cn.net.susan.entity.aftersale.RefundConditionEntity;
import cn.net.susan.entity.aftersale.RefundEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface RefundMapper extends BaseMapper<RefundEntity, RefundConditionEntity> {
	/**
     * 查询退货单信息
     *
     * @param id 退货单ID
     * @return 退货单信息
     */
	RefundEntity findById(Long id);

	/**
     * 添加退货单
     *
     * @param refundEntity 退货单信息
     * @return 结果
     */
	int insert(RefundEntity refundEntity);

	/**
     * 修改退货单
     *
     * @param refundEntity 退货单信息
     * @return 结果
     */
	int update(RefundEntity refundEntity);

    /**
     * 批量删除退货单
     *
     * @param ids id集合
     * @param entity 退货单实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") RefundEntity entity);

    /**
     * 批量查询退货单信息
     *
     * @param ids ID集合
     * @return 退货单信息
    */
    List<RefundEntity> findByIds(List<Long> ids);
}
