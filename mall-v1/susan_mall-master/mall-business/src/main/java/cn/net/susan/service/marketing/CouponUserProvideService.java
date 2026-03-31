package cn.net.susan.service.marketing;

import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.marketing.CouponUserProvideConditionEntity;
import cn.net.susan.entity.marketing.CouponUserProvideEntity;
import cn.net.susan.enums.ValidStatusEnum;
import cn.net.susan.helper.CouponUserHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.marketing.CouponUserProvideMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CouponUserProvideService extends BaseService<CouponUserProvideEntity, CouponUserProvideConditionEntity> {

    @Autowired
    private CouponUserProvideMapper couponUserProvideMapper;
    @Autowired
    private CouponUserHelper couponUserHelper;

    /**
     * 查询优惠券发放信息
     *
     * @param id 优惠券发放ID
     * @return 优惠券发放信息
     */
    public CouponUserProvideEntity findById(Long id) {
        return couponUserProvideMapper.findById(id);
    }

    /**
     * 根据条件分页查询优惠券发放列表
     *
     * @param couponUserProvideConditionEntity 优惠券发放信息
     * @return 优惠券发放集合
     */
    public ResponsePageEntity<CouponUserProvideEntity> searchByPage(CouponUserProvideConditionEntity couponUserProvideConditionEntity) {
        ResponsePageEntity<CouponUserProvideEntity> responsePageEntity = super.searchByPage(couponUserProvideConditionEntity);
        couponUserHelper.fillCouponUserInfo(responsePageEntity.getData());
        return responsePageEntity;
    }

    /**
     * 新增优惠券发放
     *
     * @param couponUserProvideEntity 优惠券发放信息
     * @return 结果
     */
    public int insert(CouponUserProvideEntity couponUserProvideEntity) {
        CouponUserProvideConditionEntity couponUserProvideConditionEntity = new CouponUserProvideConditionEntity();
        couponUserProvideConditionEntity.setCouponId(couponUserProvideEntity.getCouponId());
        List<CouponUserProvideEntity> couponUserProvideEntities = couponUserProvideMapper.searchByCondition(couponUserProvideConditionEntity);
        AssertUtil.isTrue(CollectionUtils.isEmpty(couponUserProvideEntities), "该优惠券已有发放配置");

        couponUserProvideEntity.setValidStatus(ValidStatusEnum.VALID.getValue());
        return couponUserProvideMapper.insert(couponUserProvideEntity);
    }

    /**
     * 修改优惠券发放
     *
     * @param couponUserProvideEntity 优惠券发放信息
     * @return 结果
     */
    public int update(CouponUserProvideEntity couponUserProvideEntity) {
        return couponUserProvideMapper.update(couponUserProvideEntity);
    }

    /**
     * 批量删除优惠券发放
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<CouponUserProvideEntity> entities = couponUserProvideMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "优惠券发放已被删除");

        CouponUserProvideEntity entity = new CouponUserProvideEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return couponUserProvideMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return couponUserProvideMapper;
    }
}
