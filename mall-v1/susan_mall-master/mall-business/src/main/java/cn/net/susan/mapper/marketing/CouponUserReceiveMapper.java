package cn.net.susan.mapper.marketing;

import cn.net.susan.entity.marketing.CouponUserReceiveConditionEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveEntity;
import java.util.List;

import cn.net.susan.entity.sys.UserEntity;
import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CouponUserReceiveMapper extends BaseMapper<CouponUserReceiveEntity, CouponUserReceiveConditionEntity> {
	/**
     * 查询优惠券领取信息
     *
     * @param id 优惠券领取ID
     * @return 优惠券领取信息
     */
	CouponUserReceiveEntity findById(Long id);

	/**
     * 添加优惠券领取
     *
     * @param couponUserReceiveEntity 优惠券领取信息
     * @return 结果
     */
	int insert(CouponUserReceiveEntity couponUserReceiveEntity);

	/**
     * 修改优惠券领取
     *
     * @param couponUserReceiveEntity 优惠券领取信息
     * @return 结果
     */
	int update(CouponUserReceiveEntity couponUserReceiveEntity);

    /**
     * 批量删除优惠券领取
     *
     * @param ids id集合
     * @param entity 优惠券领取实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CouponUserReceiveEntity entity);

    /**
     * 批量查询优惠券领取信息
     *
     * @param ids ID集合
     * @return 优惠券领取信息
    */
    List<CouponUserReceiveEntity> findByIds(List<Long> ids);

	/**
	 * 批量更新优惠券使用状态
	 *
	 * @param list 优惠券领取集合
	 * @return 影响行数
	 */
	int updateForBatch(List<CouponUserReceiveEntity> list);
}
