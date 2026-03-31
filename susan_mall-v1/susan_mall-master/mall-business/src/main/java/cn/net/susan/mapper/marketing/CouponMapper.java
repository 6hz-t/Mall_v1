package cn.net.susan.mapper.marketing;

import cn.net.susan.entity.marketing.CouponConditionEntity;
import cn.net.susan.entity.marketing.CouponEntity;
import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface CouponMapper extends BaseMapper<CouponEntity, CouponConditionEntity> {
	/**
     * 查询优惠券信息
     *
     * @param id 优惠券ID
     * @return 优惠券信息
     */
	CouponEntity findById(Long id);

	/**
     * 添加优惠券
     *
     * @param couponEntity 优惠券信息
     * @return 结果
     */
	int insert(CouponEntity couponEntity);

	/**
     * 修改优惠券
     *
     * @param couponEntity 优惠券信息
     * @return 结果
     */
	int update(CouponEntity couponEntity);

    /**
     * 批量删除优惠券
     *
     * @param ids id集合
     * @param entity 优惠券实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") CouponEntity entity);

    /**
     * 批量查询优惠券信息
     *
     * @param ids ID集合
     * @return 优惠券信息
    */
    List<CouponEntity> findByIds(List<Long> ids);
}
