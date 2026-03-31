package cn.net.susan.service.shopping;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.net.susan.entity.mall.ProductPhotoConditionEntity;
import cn.net.susan.entity.mall.ProductPhotoEntity;
import cn.net.susan.entity.marketing.CouponConditionEntity;
import cn.net.susan.entity.marketing.CouponEntity;
import cn.net.susan.entity.marketing.CouponUserProvideConditionEntity;
import cn.net.susan.entity.marketing.CouponUserProvideEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveConditionEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveEntity;
import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.entity.shopping.web.CouponGroupProductWebEntity;
import cn.net.susan.entity.shopping.web.ShoppingCartProductWebEntity;
import cn.net.susan.entity.shopping.web.ShoppingCartWebEntity;
import cn.net.susan.enums.CouponTypeEnum;
import cn.net.susan.enums.PhotoTypeEnum;
import cn.net.susan.helper.UserProductHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.mall.ProductPhotoMapper;
import cn.net.susan.mapper.marketing.CouponMapper;
import cn.net.susan.mapper.marketing.CouponUserProvideMapper;
import cn.net.susan.mapper.marketing.CouponUserReceiveMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.service.marketing.strategy.CouponContext;
import cn.net.susan.service.order.TradeService;
import cn.net.susan.util.CouponUtil;
import cn.net.susan.util.UuidUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.shopping.ShoppingCartMapper;
import cn.net.susan.entity.shopping.ShoppingCartConditionEntity;
import cn.net.susan.entity.shopping.ShoppingCartEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;

import static cn.net.susan.util.FillUserUtil.getCurrentUserInfo;


@Service
public class ShoppingCartService extends BaseService<ShoppingCartEntity, ShoppingCartConditionEntity> {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserProductHelper userProductHelper;
    @Autowired
    private ProductPhotoMapper productPhotoMapper;
    @Autowired
    private CouponUserReceiveMapper couponUserReceiveMapper;
    @Autowired
    private CouponUserProvideMapper couponUserProvideMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private TradeService tradeService;

    /**
     * 修改购物车
     *
     * @param shoppingCartEntity 购物车信息
     */
    public void updateShoppingCart(ShoppingCartEntity shoppingCartEntity) {
        AssertUtil.notNull(shoppingCartEntity.getId(), "id不能为空");
        AssertUtil.notNull(shoppingCartEntity.getQuantity(), "quantity不能为空");

        FillUserUtil.fillUpdateUserInfo(shoppingCartEntity);
        shoppingCartMapper.update(shoppingCartEntity);
    }

    /**
     * 添加购物车
     *
     * @param shoppingCartEntity 条件
     * @return 购物车商品列表
     */
    public Boolean addShoppingCart(ShoppingCartEntity shoppingCartEntity) {
        shoppingCartEntity.setUserId(FillUserUtil.getCurrentUserInfo().getId());

        tradeService.checkStock(Lists.newArrayList(shoppingCartEntity));

        ShoppingCartConditionEntity shoppingCartConditionEntity = new ShoppingCartConditionEntity();
        shoppingCartConditionEntity.setUserId(shoppingCartEntity.getUserId());
        shoppingCartConditionEntity.setProductId(shoppingCartEntity.getProductId());
        List<ShoppingCartEntity> shoppingCartEntities = shoppingCartMapper.searchByCondition(shoppingCartConditionEntity);
        if (CollectionUtils.isNotEmpty(shoppingCartEntities)) {
            ShoppingCartEntity oldShoppingCartEntity = shoppingCartEntities.get(0);
            oldShoppingCartEntity.setQuantity(oldShoppingCartEntity.getQuantity() + shoppingCartEntity.getQuantity());
            FillUserUtil.fillUpdateUserInfo(oldShoppingCartEntity);
            shoppingCartMapper.update(oldShoppingCartEntity);
            return Boolean.FALSE;
        } else {
            shoppingCartMapper.insert(shoppingCartEntity);
            return Boolean.TRUE;
        }
    }

    /**
     * 根据条件搜索购物车商品列表
     *
     * @param shoppingCartConditionEntity 条件
     * @return 购物车商品列表
     */
    public ShoppingCartWebEntity getShoppingCartProduct(ShoppingCartConditionEntity shoppingCartConditionEntity) {
        ShoppingCartWebEntity shoppingCartWebEntity = new ShoppingCartWebEntity();

        shoppingCartConditionEntity.setUserId(FillUserUtil.getCurrentUserInfo().getId());
        shoppingCartConditionEntity.setPageSize(0);
        ResponsePageEntity<ShoppingCartEntity> responsePageEntity = super.searchByPage(shoppingCartConditionEntity);
        //填充商品信息
        userProductHelper.fillProductInfo(responsePageEntity.getData());
        List<ShoppingCartProductWebEntity> shoppingCartProductWebEntities = convertShoppingCartProductWebEntity(responsePageEntity);
        //填充图片
        fillCover(shoppingCartProductWebEntities);
        //计算每个商品总金额
        calcTotalAmount(shoppingCartWebEntity, shoppingCartProductWebEntities);
        //计算优惠金额
        calcCouponDiscount(shoppingCartWebEntity, shoppingCartProductWebEntities);
        //计算最终支付金额
        calcFinalMoney(shoppingCartWebEntity);
        return shoppingCartWebEntity;
    }

    private void calcCouponDiscount(ShoppingCartWebEntity shoppingCartWebEntity, List<ShoppingCartProductWebEntity> shoppingCartProductWebEntities) {
        List<ShoppingCartProductWebEntity> allCouponProductWebEntities = Lists.newArrayList();
        CouponUserReceiveConditionEntity couponUserReceiveConditionEntity = new CouponUserReceiveConditionEntity();
        couponUserReceiveConditionEntity.setUseStatus(0);
        couponUserReceiveConditionEntity.setUserId(getCurrentUserInfo().getId());
        List<CouponUserReceiveEntity> couponUserReceiveEntities = couponUserReceiveMapper.searchByCondition(couponUserReceiveConditionEntity);
        if (CollectionUtils.isNotEmpty(couponUserReceiveEntities)) {
            List<Long> couponIdList = couponUserReceiveEntities.stream().map(CouponUserReceiveEntity::getCouponId).distinct().collect(Collectors.toList());
            CouponConditionEntity couponConditionEntity = new CouponConditionEntity();
            couponConditionEntity.setIdList(couponIdList);
            List<CouponEntity> couponEntities = couponMapper.searchByCondition(couponConditionEntity);

            CouponUserProvideConditionEntity couponUserProvideConditionEntity = new CouponUserProvideConditionEntity();
            couponUserProvideConditionEntity.setCouponIdList(couponIdList);
            List<CouponUserProvideEntity> couponUserProvideEntities = couponUserProvideMapper.searchByCondition(couponUserProvideConditionEntity);

            fillCouponInfo(couponUserReceiveEntities, couponEntities, couponUserProvideEntities);

            List<CouponGroupProductWebEntity> couponGroupProductEntities = Lists.newArrayList();
            couponEntities.stream().forEach(coupon -> {
                CouponGroupProductWebEntity couponGroupProductWebEntity = new CouponGroupProductWebEntity();
                List<CouponUserReceiveEntity> findUserCouponList = couponUserReceiveEntities.stream()
                        .filter(x -> coupon.getId().equals(x.getCouponId())).collect(Collectors.toList());
                List<ShoppingCartProductWebEntity> findUseCouponShoppingCartProductList = shoppingCartProductWebEntities.stream()
                        .filter(x -> findUserCouponList.stream().anyMatch(f -> f.getCouponUserProvideEntity().getProductId().equals(x.getProductId())
                                || f.getCouponUserProvideEntity().getProductId() == 0))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(findUseCouponShoppingCartProductList)) {
                    return;
                }
                couponGroupProductWebEntity.setCouponWebEntity(CouponUtil.createCouponWebEntity(coupon));
                couponGroupProductWebEntity.setShoppingCartList(findUseCouponShoppingCartProductList);
                couponGroupProductEntities.add(couponGroupProductWebEntity);
            });

            List<CouponGroupProductWebEntity> realCouponGroupProductList = calcUnionShoppingCartProduct(allCouponProductWebEntities, couponGroupProductEntities);
            shoppingCartWebEntity.setCouponGroupProductWebEntityList(realCouponGroupProductList);
        }
        CouponGroupProductWebEntity noCouponGroupProductWebEntity = addNoCouponProduct(allCouponProductWebEntities, shoppingCartProductWebEntities);
        if (Objects.nonNull(noCouponGroupProductWebEntity)) {
            List<CouponGroupProductWebEntity> couponGroupProductEntities = Lists.newArrayList(noCouponGroupProductWebEntity);
            if (CollectionUtils.isNotEmpty(shoppingCartWebEntity.getCouponGroupProductWebEntityList())) {
                couponGroupProductEntities.addAll(shoppingCartWebEntity.getCouponGroupProductWebEntityList());
            }
            shoppingCartWebEntity.setCouponGroupProductWebEntityList(couponGroupProductEntities);
        }
    }


    private List<CouponGroupProductWebEntity> calcUnionShoppingCartProduct(List<ShoppingCartProductWebEntity> shoppingCartProductWebEntities,
                                                                           List<CouponGroupProductWebEntity> couponGroupProductEntities) {
        //计算购物车中商品所有的可能优惠
        for (CouponGroupProductWebEntity couponGroupProductWebEntity : couponGroupProductEntities) {
            List<ShoppingCartProductWebEntity> shoppingCartList = couponGroupProductWebEntity.getShoppingCartList();
            List<ShoppingCartProductWebEntity> newShoppingCartList = Lists.newArrayList();
            for (ShoppingCartProductWebEntity shoppingCartProductWebEntity : shoppingCartList) {
                BigDecimal payPrice = shoppingCartProductWebEntity.getPrice();
                if (CouponTypeEnum.FULL_COUNT_DISCOUNT.getValue().equals(couponGroupProductWebEntity.getCouponWebEntity().getType())) {
                    if (shoppingCartProductWebEntity.getQuantity() >= (couponGroupProductWebEntity.getCouponWebEntity().getMinProductCount())) {
                        payPrice = CouponContext.getInstance().calcPayMoney(shoppingCartProductWebEntity.getPrice(),
                                couponGroupProductWebEntity.getCouponWebEntity());
                    }
                } else {
                    payPrice = CouponContext.getInstance().calcPayMoney(shoppingCartProductWebEntity.getPrice(),
                            couponGroupProductWebEntity.getCouponWebEntity());
                }
                shoppingCartProductWebEntity.setPayPrice(payPrice);

                //解决引用传递的问题
                ShoppingCartProductWebEntity newShoppingCartProductWebEntity = new ShoppingCartProductWebEntity();
                BeanUtil.copyProperties(shoppingCartProductWebEntity, newShoppingCartProductWebEntity, false);
                newShoppingCartProductWebEntity.setUuid(UuidUtil.getUuid());
                newShoppingCartProductWebEntity.setPayAmount(getAmount(payPrice, shoppingCartProductWebEntity.getQuantity()));
                shoppingCartProductWebEntities.add(newShoppingCartProductWebEntity);
                newShoppingCartList.add(newShoppingCartProductWebEntity);
            }
            couponGroupProductWebEntity.setShoppingCartList(newShoppingCartList);
        }

        Map<Long, List<ShoppingCartProductWebEntity>> productMap = shoppingCartProductWebEntities.stream()
                .collect(Collectors.groupingBy(ShoppingCartProductWebEntity::getProductId));
        //计算同一个商品，如果有多种优惠，优惠金额最大的是哪种优惠
        List<ShoppingCartProductWebEntity> minShoppingCartProductWebEntities = Lists.newArrayList();
        productMap.forEach((key, productList) -> {
            Optional<ShoppingCartProductWebEntity> minValueOptional = productList.stream().min((a, b) -> a.getPayAmount().compareTo(b.getPayAmount()));
            if (minValueOptional.isPresent()) {
                minShoppingCartProductWebEntities.add(minValueOptional.get());
            }
        });

        //合并同一个商品，只保留最大的优惠
        Map<String, List<ShoppingCartProductWebEntity>> uuidProductMap = minShoppingCartProductWebEntities.stream().collect(Collectors.groupingBy(ShoppingCartProductWebEntity::getUuid));
        List<CouponGroupProductWebEntity> result = Lists.newArrayList();
        for (CouponGroupProductWebEntity couponGroupProductWebEntity : couponGroupProductEntities) {
            List<ShoppingCartProductWebEntity> shoppingCartList = couponGroupProductWebEntity.getShoppingCartList();
            boolean firstFind = false;
            for (int i = 0; i < shoppingCartList.size(); i++) {
                ShoppingCartProductWebEntity shoppingCartProductWebEntity = shoppingCartList.get(i);
                List<ShoppingCartProductWebEntity> findList = uuidProductMap.get(shoppingCartProductWebEntity.getUuid());
                if (CollectionUtils.isNotEmpty(findList)) {
                    ShoppingCartProductWebEntity findShoppingCartProductWebEntity = findList.get(0);
                    if (!firstFind) {
                        result.add(couponGroupProductWebEntity);
                        couponGroupProductWebEntity.setShoppingCartList(Lists.newArrayList(findShoppingCartProductWebEntity));
                        firstFind = true;
                    } else {
                        couponGroupProductWebEntity.getShoppingCartList().add(findShoppingCartProductWebEntity);
                    }
                }
            }
        }

        return result;
    }

    private BigDecimal getAmount(BigDecimal price, int quantity) {
        return price.multiply(new BigDecimal(quantity));
    }

    private CouponGroupProductWebEntity addNoCouponProduct(List<ShoppingCartProductWebEntity> allCouponProductWebEntities,
                                                           List<ShoppingCartProductWebEntity> shoppingCartProductWebEntities) {
        List<ShoppingCartProductWebEntity> noCouponProductList = shoppingCartProductWebEntities.stream()
                .filter(x -> allCouponProductWebEntities.stream().noneMatch(a -> a.getProductId().equals(x.getProductId())))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(noCouponProductList)) {
            return null;
        }
        CouponGroupProductWebEntity couponGroupProductWebEntity = new CouponGroupProductWebEntity();
        couponGroupProductWebEntity.setCouponWebEntity(new CouponWebEntity());
        couponGroupProductWebEntity.setShoppingCartList(noCouponProductList);
        return couponGroupProductWebEntity;
    }

    private void fillCouponInfo(List<CouponUserReceiveEntity> couponUserReceiveEntities,
                                List<CouponEntity> couponEntities,
                                List<CouponUserProvideEntity> couponUserProvideEntities) {
        for (CouponUserReceiveEntity couponUserReceiveEntity : couponUserReceiveEntities) {
            Optional<CouponEntity> couponEntityOptional = couponEntities.stream()
                    .filter(x -> x.getId().equals(couponUserReceiveEntity.getCouponId())).findFirst();
            if (couponEntityOptional.isPresent()) {
                couponUserReceiveEntity.setCouponEntity(couponEntityOptional.get());
            }

            Optional<CouponUserProvideEntity> couponUserProvideEntityOptional = couponUserProvideEntities.stream()
                    .filter(x -> x.getCouponId().equals(couponUserReceiveEntity.getCouponId())).findFirst();
            if (couponUserProvideEntityOptional.isPresent()) {
                couponUserReceiveEntity.setCouponUserProvideEntity(couponUserProvideEntityOptional.get());
            }
        }
    }

    private void calcTotalAmount(ShoppingCartWebEntity shoppingCartWebEntity, List<ShoppingCartProductWebEntity> shoppingCartProductWebEntities) {
        BigDecimal totalMoney = BigDecimal.ZERO;
        for (ShoppingCartProductWebEntity shoppingCartProductWebEntity : shoppingCartProductWebEntities) {
            BigDecimal totalAmount = getAmount(shoppingCartProductWebEntity.getPrice(), shoppingCartProductWebEntity.getQuantity());
            shoppingCartProductWebEntity.setTotalAmount(totalAmount);
            shoppingCartProductWebEntity.setPayAmount(totalAmount);
            shoppingCartProductWebEntity.setPayPrice(shoppingCartProductWebEntity.getPrice());
            totalMoney = totalMoney.add(totalAmount);
        }
        shoppingCartWebEntity.setTotalMoney(totalMoney);
    }

    private void calcFinalMoney(ShoppingCartWebEntity shoppingCartWebEntity) {
        if (CollectionUtils.isEmpty(shoppingCartWebEntity.getCouponGroupProductWebEntityList())) {
            return;
        }
        BigDecimal finalMoney = BigDecimal.ZERO;
        for (CouponGroupProductWebEntity couponGroupProductWebEntity : shoppingCartWebEntity.getCouponGroupProductWebEntityList()) {
            for (ShoppingCartProductWebEntity shoppingCartProductWebEntity : couponGroupProductWebEntity.getShoppingCartList()) {
                finalMoney = finalMoney.add(shoppingCartProductWebEntity.getPayAmount());
            }
        }
        shoppingCartWebEntity.setSubtractMoney(shoppingCartWebEntity.getTotalMoney().subtract(finalMoney));
        shoppingCartWebEntity.setFinalMoney(finalMoney);
    }

    private List<ShoppingCartProductWebEntity> convertShoppingCartProductWebEntity(ResponsePageEntity<ShoppingCartEntity> responsePageEntity) {
        if (CollectionUtils.isEmpty(responsePageEntity.getData())) {
            return Collections.emptyList();
        }

        return responsePageEntity.getData().stream().map(x -> {
            ShoppingCartProductWebEntity shoppingCartProductWebEntity = new ShoppingCartProductWebEntity();
            shoppingCartProductWebEntity.setId(x.getId());
            shoppingCartProductWebEntity.setProductId(x.getProductId());
            shoppingCartProductWebEntity.setProductName(x.getProductName());
            shoppingCartProductWebEntity.setModel(x.getModel());
            shoppingCartProductWebEntity.setPrice(x.getPrice());
            shoppingCartProductWebEntity.setQuantity(x.getQuantity());
            shoppingCartProductWebEntity.setCover(x.getCoverUrl());
            shoppingCartProductWebEntity.setCreateTime(x.getCreateTime());
            shoppingCartProductWebEntity.setTotalAmount(x.getTotalAmount());
            shoppingCartProductWebEntity.setStock(x.getStock());
            return shoppingCartProductWebEntity;
        }).collect(Collectors.toList());
    }

    private void fillCover(List<ShoppingCartProductWebEntity> dataList) {
        List<Long> productIdList = dataList.stream().map(ShoppingCartProductWebEntity::getProductId).distinct().collect(Collectors.toList());
        ProductPhotoConditionEntity productPhotoConditionEntity = new ProductPhotoConditionEntity();
        productPhotoConditionEntity.setProductIdList(productIdList);
        productPhotoConditionEntity.setType(PhotoTypeEnum.COVER.getValue());
        List<ProductPhotoEntity> productPhotoEntities = productPhotoMapper.searchByCondition(productPhotoConditionEntity);
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(productPhotoEntities)) {
            return;
        }

        for (ShoppingCartProductWebEntity shoppingCartProductWebEntity : dataList) {
            Optional<ProductPhotoEntity> photoEntityOptional = productPhotoEntities.stream()
                    .filter(x -> shoppingCartProductWebEntity.getProductId().equals(x.getProductId())).findAny();
            if (photoEntityOptional.isPresent()) {
                shoppingCartProductWebEntity.setCover(photoEntityOptional.get().getUrl());
            }
        }
    }

    /**
     * 查询购物车信息
     *
     * @param id 购物车ID
     * @return 购物车信息
     */
    public ShoppingCartEntity findById(Long id) {
        return shoppingCartMapper.findById(id);
    }

    /**
     * 根据条件分页查询购物车列表
     *
     * @param shoppingCartConditionEntity 购物车信息
     * @return 购物车集合
     */
    public ResponsePageEntity<ShoppingCartEntity> searchByPage(ShoppingCartConditionEntity shoppingCartConditionEntity) {
        ResponsePageEntity<ShoppingCartEntity> responsePageEntity = super.searchByPage(shoppingCartConditionEntity);
        userProductHelper.fillUserProductInfo(responsePageEntity.getData());
        if (CollectionUtils.isNotEmpty(responsePageEntity.getData())) {
            for (ShoppingCartEntity shoppingCartEntity : responsePageEntity.getData()) {
                shoppingCartEntity.setTotalAmount(new BigDecimal(shoppingCartEntity.getQuantity()).multiply(shoppingCartEntity.getPrice()));
            }
        }
        return responsePageEntity;
    }


    /**
     * 新增购物车
     *
     * @param shoppingCartEntity 购物车信息
     * @return 结果
     */
    public int insert(ShoppingCartEntity shoppingCartEntity) {
        userProductHelper.checkParam(shoppingCartEntity);
        return shoppingCartMapper.insert(shoppingCartEntity);
    }

    /**
     * 修改购物车
     *
     * @param shoppingCartEntity 购物车信息
     * @return 结果
     */
    public int update(ShoppingCartEntity shoppingCartEntity) {
        userProductHelper.checkParam(shoppingCartEntity);

        FillUserUtil.fillUpdateUserInfo(shoppingCartEntity);
        return shoppingCartMapper.update(shoppingCartEntity);
    }


    /**
     * 批量删除购物车
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<ShoppingCartEntity> entities = shoppingCartMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "购物车已被删除");

        ShoppingCartEntity entity = new ShoppingCartEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return shoppingCartMapper.deleteByIds(ids, entity);
    }


    @Override
    protected BaseMapper getBaseMapper() {
        return shoppingCartMapper;
    }
}
