package cn.net.susan.mapper.aftersale;

import cn.net.susan.entity.aftersale.RefundAddressConditionEntity;
import cn.net.susan.entity.aftersale.RefundAddressEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface RefundAddressMapper extends BaseMapper<RefundAddressEntity, RefundAddressConditionEntity> {
	/**
     * 查询退货单地址信息
     *
     * @param id 退货单地址ID
     * @return 退货单地址信息
     */
	RefundAddressEntity findById(Long id);

	/**
     * 添加退货单地址
     *
     * @param refundAddressEntity 退货单地址信息
     * @return 结果
     */
	int insert(RefundAddressEntity refundAddressEntity);

	/**
     * 修改退货单地址
     *
     * @param refundAddressEntity 退货单地址信息
     * @return 结果
     */
	int update(RefundAddressEntity refundAddressEntity);

    /**
     * 批量删除退货单地址
     *
     * @param ids id集合
     * @param entity 退货单地址实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") RefundAddressEntity entity);

    /**
     * 批量查询退货单地址信息
     *
     * @param ids ID集合
     * @return 退货单地址信息
    */
    List<RefundAddressEntity> findByIds(List<Long> ids);
}
