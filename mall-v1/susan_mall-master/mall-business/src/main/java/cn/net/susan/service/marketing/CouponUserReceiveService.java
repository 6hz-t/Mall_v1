package cn.net.susan.service.marketing;

import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveConditionEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveEntity;
import cn.net.susan.helper.CouponUserHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.marketing.CouponUserReceiveMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CouponUserReceiveService extends BaseService<CouponUserReceiveEntity, CouponUserReceiveConditionEntity> {

    @Autowired
    private CouponUserReceiveMapper couponUserReceiveMapper;
    @Autowired
    private CouponUserHelper couponUserHelper;

    /**
     * 查询优惠券领取信息
     *
     * @param id 优惠券领取ID
     * @return 优惠券领取信息
     */
    public CouponUserReceiveEntity findById(Long id) {
        return couponUserReceiveMapper.findById(id);
    }

    /**
     * 根据条件分页查询优惠券领取列表
     *
     * @param couponUserReceiveConditionEntity 优惠券领取信息
     * @return 优惠券领取集合
     */
    public ResponsePageEntity<CouponUserReceiveEntity> searchByPage(CouponUserReceiveConditionEntity couponUserReceiveConditionEntity) {
        ResponsePageEntity<CouponUserReceiveEntity> responsePageEntity = super.searchByPage(couponUserReceiveConditionEntity);
        couponUserHelper.fillCouponUserInfo(responsePageEntity.getData());
        return responsePageEntity;
    }

    /**
     * 新增优惠券领取
     *
     * @param couponUserReceiveEntity 优惠券领取信息
     * @return 结果
     */
    public int insert(CouponUserReceiveEntity couponUserReceiveEntity) {
        return couponUserReceiveMapper.insert(couponUserReceiveEntity);
    }

    /**
     * 修改优惠券领取
     *
     * @param couponUserReceiveEntity 优惠券领取信息
     * @return 结果
     */
    public int update(CouponUserReceiveEntity couponUserReceiveEntity) {
        return couponUserReceiveMapper.update(couponUserReceiveEntity);
    }

    /**
     * 批量删除优惠券领取
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<CouponUserReceiveEntity> entities = couponUserReceiveMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "优惠券领取已被删除");

        CouponUserReceiveEntity entity = new CouponUserReceiveEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return couponUserReceiveMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return couponUserReceiveMapper;
    }
}
