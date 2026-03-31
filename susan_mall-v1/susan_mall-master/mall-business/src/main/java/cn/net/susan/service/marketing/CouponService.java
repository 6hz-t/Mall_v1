package cn.net.susan.service.marketing;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.web.ProductWebEntity;
import cn.net.susan.entity.marketing.CouponUserProvideConditionEntity;
import cn.net.susan.entity.marketing.CouponUserProvideEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveConditionEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveEntity;
import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.enums.ValidStatusEnum;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.mapper.marketing.CouponUserProvideMapper;
import cn.net.susan.mapper.marketing.CouponUserReceiveMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.DateFormatUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.marketing.CouponMapper;
import cn.net.susan.entity.marketing.CouponConditionEntity;
import cn.net.susan.entity.marketing.CouponEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;
import org.springframework.transaction.support.TransactionTemplate;

import javax.validation.constraints.NotNull;

import static cn.net.susan.util.DateFormatUtil.YYYY_MM_DD;



@Service
public class CouponService extends BaseService<CouponEntity, CouponConditionEntity> {

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponUserReceiveMapper couponUserReceiveMapper;
    @Autowired
    private CouponUserProvideMapper couponUserProvideMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 获取可领取的优惠券列表
     *
     * @return 优惠券列表
     */
    public List<CouponWebEntity> getObtainableCouponList() {
        CouponUserProvideConditionEntity couponUserProvideConditionEntity = new CouponUserProvideConditionEntity();
        couponUserProvideConditionEntity.setValidStatus(ValidStatusEnum.VALID.getValue());
        List<CouponUserProvideEntity> couponUserProvideEntities = couponUserProvideMapper.searchByCondition(couponUserProvideConditionEntity);
        if (CollectionUtils.isEmpty(couponUserProvideEntities)) {
            return Collections.emptyList();
        }

        List<Long> productIdList = couponUserProvideEntities.stream().filter(x -> x.getProductId() > 0)
                .map(CouponUserProvideEntity::getProductId).collect(Collectors.toList());
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setIdList(productIdList);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        Map<Long, List<ProductEntity>> productMap = productEntities.stream().collect(Collectors.groupingBy(ProductEntity::getId));

        List<Long> couponIdList = couponUserProvideEntities.stream()
                .map(CouponUserProvideEntity::getCouponId).collect(Collectors.toList());

        CouponConditionEntity couponConditionEntity = new CouponConditionEntity();
        couponConditionEntity.setValidStatus(ValidStatusEnum.VALID.getValue());
        couponConditionEntity.setIdList(couponIdList);
        couponConditionEntity.setPageSize(0);
        List<CouponEntity> couponEntities = couponMapper.searchByCondition(couponConditionEntity);
        if (CollectionUtils.isEmpty(couponEntities)) {
            return Collections.emptyList();
        }

        List<CouponWebEntity> result = Lists.newArrayList();
        for (CouponUserProvideEntity couponUserProvideEntity : couponUserProvideEntities) {
            Optional<CouponEntity> couponOptional = couponEntities.stream().filter(x -> x.getId().equals(couponUserProvideEntity.getCouponId())).findFirst();
            if (couponOptional.isPresent()) {
                CouponEntity couponEntity = couponOptional.get();
                CouponWebEntity couponWebEntity = new CouponWebEntity();
                BeanUtil.copyProperties(couponEntity, couponWebEntity, true);
                couponWebEntity.setProductId(couponUserProvideEntity.getProductId());
                List<ProductEntity> findProductList = productMap.get(couponWebEntity.getProductId());
                if (CollectionUtils.isNotEmpty(findProductList)) {
                    couponWebEntity.setProductName(findProductList.get(0).getName());
                }
                couponWebEntity.setUserId(couponUserProvideEntity.getUserId());
                long days = DateUtil.between(couponEntity.getUseEndTime(), new Date(), DateUnit.DAY);
                couponWebEntity.setValidDays(days >= 0 ? (int) days : 0);
                couponWebEntity.setUseEndTimeStr(DateFormatUtil.parseToString(couponEntity.getUseEndTime(), YYYY_MM_DD));
                couponWebEntity.setId(couponUserProvideEntity.getId());
                couponWebEntity.setCouponId(couponUserProvideEntity.getCouponId());
                result.add(couponWebEntity);
            }
        }
        fillCurrentUserReceived(result);
        return result;
    }

    private void fillCurrentUserReceived(List<CouponWebEntity> result) {
        List<Long> couponIdList = result.stream().map(CouponWebEntity::getCouponId).distinct().collect(Collectors.toList());
        JwtUserEntity currentUserInfoOrNull = FillUserUtil.getCurrentUserInfoOrNull();
        if (Objects.isNull(currentUserInfoOrNull)) {
            return;
        }

        CouponUserReceiveConditionEntity couponUserReceiveConditionEntity = new CouponUserReceiveConditionEntity();
        couponUserReceiveConditionEntity.setUserId(currentUserInfoOrNull.getId());
        couponUserReceiveConditionEntity.setCouponIdList(couponIdList);
        List<CouponUserReceiveEntity> couponUserReceiveEntities = couponUserReceiveMapper.searchByCondition(couponUserReceiveConditionEntity);
        for (CouponWebEntity couponWebEntity : result) {
            boolean match = couponUserReceiveEntities.stream().anyMatch(x -> x.getCouponId().equals(couponWebEntity.getCouponId()));
            couponWebEntity.setCurrentUserReceived(match);
        }
    }

    /**
     * 获取某用户已经领取的优惠券列表
     *
     * @return 商品列表
     */
    public List<CouponWebEntity> getUserCouponList() {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        CouponUserReceiveConditionEntity couponUserReceiveConditionEntity = new CouponUserReceiveConditionEntity();
        couponUserReceiveConditionEntity.setUserId(currentUserInfo.getId());
        couponUserReceiveConditionEntity.setPageSize(0);
        List<CouponUserReceiveEntity> couponUserReceiveEntities = couponUserReceiveMapper.searchByCondition(couponUserReceiveConditionEntity);
        if (CollectionUtils.isEmpty(couponUserReceiveEntities)) {
            return Collections.emptyList();
        }

        CouponConditionEntity couponConditionEntity = new CouponConditionEntity();
        couponConditionEntity.setIdList(couponUserReceiveEntities.stream().map(CouponUserReceiveEntity::getCouponId).distinct().collect(Collectors.toList()));
        List<CouponEntity> couponEntities = couponMapper.searchByCondition(couponConditionEntity);

        return couponUserReceiveEntities.stream().map(x -> {
            CouponWebEntity couponWebEntity = new CouponWebEntity();
            Optional<CouponEntity> couponEntityOptional = couponEntities.stream().filter(c -> c.getId().equals(x.getCouponId())).findFirst();
            if (couponEntityOptional.isPresent()) {
                CouponEntity couponEntity = couponEntityOptional.get();
                BeanUtil.copyProperties(couponEntity, couponWebEntity, true);
                couponWebEntity.setUseStartTimeStr(DateFormatUtil.parseToString(couponEntity.getUseStartTime()));
                couponWebEntity.setUseEndTimeStr(DateFormatUtil.parseToString(couponEntity.getUseEndTime()));
                long days = DateUtil.between(couponEntity.getUseEndTime(), new Date(), DateUnit.DAY);
                couponWebEntity.setValidDays(days >= 0 ? (int) days : 0);
            }
            return couponWebEntity;
        }).collect(Collectors.toList());
    }

    /**
     * 用户领取优惠券
     *
     * @param couponWebEntity 优惠券
     */
    public void receiveCoupon(CouponWebEntity couponWebEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        CouponUserProvideEntity couponUserProvideEntity = couponUserProvideMapper.findById(couponWebEntity.getId());
        AssertUtil.notNull(couponUserProvideEntity, "该优惠券在系统中已不存在");
        AssertUtil.isTrue(ValidStatusEnum.VALID.getValue().equals(couponUserProvideEntity.getValidStatus()), "该优惠券已经变成无效状态了");

        CouponEntity couponEntity = couponMapper.findById(couponUserProvideEntity.getCouponId());
        AssertUtil.notNull(couponEntity, "该优惠券在系统中已不存在");
        AssertUtil.isTrue(ValidStatusEnum.VALID.getValue().equals(couponEntity.getValidStatus()), "该优惠券已经变成无效状态了");

        CouponUserReceiveConditionEntity couponUserReceiveConditionEntity = new CouponUserReceiveConditionEntity();
        couponUserReceiveConditionEntity.setCouponId(couponUserProvideEntity.getCouponId());
        couponUserReceiveConditionEntity.setUserId(currentUserInfo.getId());
        List<CouponUserReceiveEntity> couponUserReceiveEntities = couponUserReceiveMapper.searchByCondition(couponUserReceiveConditionEntity);
        AssertUtil.isTrue(CollectionUtils.isEmpty(couponUserReceiveEntities), "该优惠券你已经领取过");

        CouponUserReceiveEntity couponUserReceiveEntity = createCouponUserReceiveEntity(currentUserInfo, couponUserProvideEntity);

        transactionTemplate.execute((status -> {
            //加行锁
            CouponUserProvideEntity oldCouponUserProvideEntity = couponUserProvideMapper.findByIdForUpdate(couponWebEntity.getId());
            AssertUtil.notNull(oldCouponUserProvideEntity, "该优惠券在系统中已不存在");
            AssertUtil.isTrue(ValidStatusEnum.VALID.getValue().equals(oldCouponUserProvideEntity.getValidStatus()), "该优惠券已经变成无效状态了");
            oldCouponUserProvideEntity.setReceiveCount(oldCouponUserProvideEntity.getReceiveCount() + 1);
            couponUserProvideMapper.update(oldCouponUserProvideEntity);
            couponUserReceiveMapper.insert(couponUserReceiveEntity);
            return Boolean.TRUE;
        }));
    }

    private CouponUserReceiveEntity createCouponUserReceiveEntity(JwtUserEntity currentUserInfo, CouponUserProvideEntity couponUserProvideEntity) {
        CouponUserReceiveEntity couponUserReceiveEntity = new CouponUserReceiveEntity();
        couponUserReceiveEntity.setCouponId(couponUserProvideEntity.getCouponId());
        couponUserReceiveEntity.setUserId(currentUserInfo.getId());
        couponUserReceiveEntity.setUseStatus(0);
        FillUserUtil.fillCreateUserInfo(couponUserReceiveEntity);
        return couponUserReceiveEntity;
    }

    /**
     * 查询优惠券信息
     *
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    public CouponEntity findById(Long id) {
        return couponMapper.findById(id);
    }

    /**
     * 根据条件分页查询优惠券列表
     *
     * @param couponConditionEntity 优惠券信息
     * @return 优惠券集合
     */
    public ResponsePageEntity<CouponEntity> searchByPage(CouponConditionEntity couponConditionEntity) {
        return super.searchByPage(couponConditionEntity);
    }

    /**
     * 新增优惠券
     *
     * @param couponEntity 优惠券信息
     * @return 结果
     */
    public int insert(CouponEntity couponEntity) {
        couponEntity.setValidStatus(ValidStatusEnum.VALID.getValue());
        return couponMapper.insert(couponEntity);
    }

    /**
     * 修改优惠券
     *
     * @param couponEntity 优惠券信息
     * @return 结果
     */
    public int update(CouponEntity couponEntity) {
        return couponMapper.update(couponEntity);
    }

    /**
     * 批量删除优惠券
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<CouponEntity> entities = couponMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "优惠券已被删除");

        CouponEntity entity = new CouponEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return couponMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return couponMapper;
    }
}
