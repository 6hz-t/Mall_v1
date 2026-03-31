package cn.net.susan.mapper.marketing;

import cn.net.susan.entity.marketing.CouponUserProvideConditionEntity;
import cn.net.susan.entity.marketing.CouponUserProvideEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CouponUserProvideMapper extends BaseMapper<CouponUserProvideEntity, CouponUserProvideConditionEntity> {
	/**
     * 查询优惠券发放信息
     *
     * @param id 优惠券发放ID
     * @return 优惠券发放信息
     */
	CouponUserProvideEntity findById(Long id);

	/**
	 * 查询优惠券发放信息，加行锁
	 * @param id
	 * @return
	 */
	CouponUserProvideEntity findByIdForUpdate(Long id);

	/**
     * 添加优惠券发放
     *
     * @param couponUserProvideEntity 优惠券发放信息
     * @return 结果
     */
	int insert(CouponUserProvideEntity couponUserProvideEntity);

	/**
     * 修改优惠券发放
     *
     * @param couponUserProvideEntity 优惠券发放信息
     * @return 结果
     */
	int update(CouponUserProvideEntity couponUserProvideEntity);

    /**
     * 批量删除优惠券发放
     *
     * @param ids id集合
     * @param entity 优惠券发放实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CouponUserProvideEntity entity);

    /**
     * 批量查询优惠券发放信息
     *
     * @param ids ID集合
     * @return 优惠券发放信息
    */
    List<CouponUserProvideEntity> findByIds(List<Long> ids);
}
