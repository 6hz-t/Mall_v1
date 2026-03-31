package cn.net.susan.service.order;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.marketing.CouponConditionEntity;
import cn.net.susan.entity.marketing.CouponEntity;
import cn.net.susan.entity.marketing.CouponUserProvideConditionEntity;
import cn.net.susan.entity.marketing.CouponUserProvideEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveConditionEntity;
import cn.net.susan.entity.marketing.CouponUserReceiveEntity;
import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.entity.order.TradeConditionEntity;
import cn.net.susan.entity.order.TradeDeliveryAddressEntity;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.entity.order.TradeItemEntity;
import cn.net.susan.entity.order.web.TradeDetailWebEntity;
import cn.net.susan.entity.order.web.TradeItemReqWebEntity;
import cn.net.susan.entity.order.web.TradeOperateWebEntity;
import cn.net.susan.entity.order.web.TradeConditionWebEntity;
import cn.net.susan.entity.order.web.TradeConfirmItemReqWebEntity;
import cn.net.susan.entity.order.web.TradeConfirmReqWebEntity;
import cn.net.susan.entity.order.web.TradeCouponWebEntity;
import cn.net.susan.entity.order.web.TradeItemWebEntity;
import cn.net.susan.entity.order.web.TradeConfirmWebEntity;
import cn.net.susan.entity.order.web.TradeSubmitWebEntity;
import cn.net.susan.entity.order.web.TradeWebEntity;
import cn.net.susan.entity.order.web.UserTradeCountWebEntity;
import cn.net.susan.entity.seckill.UserSeckillProductTradeEntity;
import cn.net.susan.entity.shopping.DeliveryAddressConditionEntity;
import cn.net.susan.entity.shopping.DeliveryAddressEntity;
import cn.net.susan.entity.shopping.ShoppingCartConditionEntity;
import cn.net.susan.entity.shopping.ShoppingCartEntity;
import cn.net.susan.entity.shopping.web.DeliveryAddressWebEntity;
import cn.net.susan.entity.sys.UserConditionEntity;
import cn.net.susan.entity.sys.UserEntity;
import cn.net.susan.enums.DelEnum;
import cn.net.susan.enums.OrderStatusEnum;
import cn.net.susan.enums.OrderTypeEnum;
import cn.net.susan.enums.PayStatusEnum;
import cn.net.susan.enums.ValidStatusEnum;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.helper.IdGenerateHelper;
import cn.net.susan.helper.IpWhiteListHelper;
import cn.net.susan.helper.MqHelper;
import cn.net.susan.helper.UserProductHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.mapper.marketing.CouponMapper;
import cn.net.susan.mapper.marketing.CouponUserProvideMapper;
import cn.net.susan.mapper.marketing.CouponUserReceiveMapper;
import cn.net.susan.mapper.order.TradeMapper;
import cn.net.susan.mapper.shopping.DeliveryAddressMapper;
import cn.net.susan.mapper.shopping.ShoppingCartMapper;
import cn.net.susan.mapper.sys.UserMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.service.marketing.CouponService;
import cn.net.susan.service.marketing.strategy.CouponContext;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.CouponUtil;
import cn.net.susan.util.DateFormatUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.util.OrderCodeUtil;
import cn.net.susan.util.RedisUtil;
import cn.net.susan.util.RedissonUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static cn.net.susan.config.RabbitConfig.TRADE_STATUS_CHANGE_ROUTING_KEY_PREFIX;
import static cn.net.susan.util.OrderCodeUtil.ORDER_CODE_LENGTH;


@Service
public class TradeService extends BaseService<TradeEntity, TradeConditionEntity> {
    private static final String REDUCE_STOCK_LOCK_PREFIX = "reduceStockLock:";

    @Autowired
    private TradeMapper tradeMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IdGenerateHelper idGenerateHelper;
    @Autowired
    private TradeSaveService tradeSaveService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MqHelper mqHelper;
    @Autowired
    private IpWhiteListHelper ipWhiteListHelper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserProductHelper userProductHelper;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponUserProvideMapper couponUserProvideMapper;
    @Autowired
    private CouponUserReceiveMapper couponUserReceiveMapper;
    @Autowired
    private DeliveryAddressMapper deliveryAddressMapper;
    @Autowired
    private TradeDeliveryAddressService tradeDeliveryAddressService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private TradeItemService tradeItemService;
    @Autowired
    private RedissonUtil redissonUtil;

    @Value("${mall.mgt.httpRequest.secretKey:susan123}")
    private String secretKey;

    @Value("${mall.mgt.tradeStatusChangeTopic:TRADE_STATUS_CHANGE_TOPIC}")
    private String tradeStatusChangeTopic;

    @Value("${mall.mgt.overTimeCancelTradeTopic:OVER_TIME_CANCEL_TRADE_TOPIC}")
    private String overTimeCancelTradeTopic;

    @Value("${mall.mgt.confirmTradeExpireTime:3600}")
    private int confirmTradeExpireTime;

    @Value("${mall.mgt.reduceStockLockSeconds:20}")
    private int reduceStockLockSeconds;

    private TradeService getSelfProxy() {
        return ((TradeService) AopContext.currentProxy());
    }

    /**
     * 获取订单明细
     *
     * @param tradeItemReqWebEntity 查询条件
     * @return 订单明细
     */
    public TradeItemWebEntity getTradeItem(@RequestBody TradeItemReqWebEntity tradeItemReqWebEntity) {
        TradeItemWebEntity tradeItemWebEntity = new TradeItemWebEntity();
        TradeItemEntity tradeItemEntity = tradeItemService.findByTradeCodeAndId(tradeItemReqWebEntity.getTradeCode(), tradeItemReqWebEntity.getTradeItemId());
        if (Objects.isNull(tradeItemEntity)) {
            return tradeItemWebEntity;
        }

        BeanUtil.copyProperties(tradeItemEntity, tradeItemWebEntity, false);
        tradeItemWebEntity.setPayAmount(tradeItemWebEntity.getAmount());
        return tradeItemWebEntity;
    }

    /**
     * 获取用户订单数量统计
     *
     * @return 用户订单数量统计
     */
    public UserTradeCountWebEntity getUserOrderTradeCount() {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        AssertUtil.notNull(currentUserInfo, "请先登录");

        UserTradeCountWebEntity userTradeCountWebEntity = new UserTradeCountWebEntity();
        TradeConditionEntity tradeConditionEntity = new TradeConditionEntity();
        tradeConditionEntity.setUserId(currentUserInfo.getId());
        List<TradeEntity> tradeEntities = getSelfProxy().searchByCondition(tradeConditionEntity);

        userTradeCountWebEntity.setWaitPayCount(getCount(tradeEntities, OrderStatusEnum.CREATE));
        userTradeCountWebEntity.setPayCount(getCount(tradeEntities, OrderStatusEnum.PAY));
        userTradeCountWebEntity.setShippedCount(getCount(tradeEntities, OrderStatusEnum.SHIPPED));
        userTradeCountWebEntity.setFinishCount(getCount(tradeEntities, OrderStatusEnum.FINISH));
        return userTradeCountWebEntity;
    }

    private int getCount(List<TradeEntity> tradeEntities, OrderStatusEnum orderStatusEnum) {
        return (int) tradeEntities.stream().filter(x -> x.getOrderStatus().equals(orderStatusEnum.getValue())).count();
    }

    /**
     * 根据code查询订单详情
     *
     * @param code 订单编号
     * @return 订单详情
     */
    public TradeDetailWebEntity getDetail(String code) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        AssertUtil.notNull(currentUserInfo, "请先登录");

        TradeDetailWebEntity tradeDetailWebEntity = new TradeDetailWebEntity();
        TradeConditionEntity tradeConditionEntity = new TradeConditionEntity();
        tradeConditionEntity.setCode(code);
        List<TradeEntity> tradeEntities = getSelfProxy().searchByCondition(tradeConditionEntity);
        if (CollectionUtils.isEmpty(tradeEntities)) {
            return tradeDetailWebEntity;
        }

        TradeEntity tradeEntity = tradeEntities.get(0);
        fillDeliveryAddress(tradeDetailWebEntity, tradeEntity);
        fillTradeBaseInfo(tradeDetailWebEntity, tradeEntity);
        fillTradeItem(tradeDetailWebEntity, tradeEntity);
        return tradeDetailWebEntity;
    }

    private void fillDeliveryAddress(TradeDetailWebEntity tradeDetailWebEntity, TradeEntity tradeEntity) {
        List<TradeDeliveryAddressEntity> deliveryAddressEntityList = tradeDeliveryAddressService.findByTradeId(tradeEntity.getId());
        if (CollectionUtils.isEmpty(deliveryAddressEntityList)) {
            return;
        }

        TradeDeliveryAddressEntity tradeDeliveryAddressEntity = deliveryAddressEntityList.get(0);
        DeliveryAddressWebEntity deliveryAddressWebEntity = new DeliveryAddressWebEntity();
        BeanUtil.copyProperties(tradeDeliveryAddressEntity, deliveryAddressWebEntity, false);
        tradeDetailWebEntity.setDeliveryAddressWebEntity(deliveryAddressWebEntity);
    }

    private void fillTradeBaseInfo(TradeDetailWebEntity tradeDetailWebEntity, TradeEntity tradeEntity) {
        tradeDetailWebEntity.setCode(tradeEntity.getCode());
        tradeDetailWebEntity.setOrderStatus(tradeEntity.getOrderStatus());
        tradeDetailWebEntity.setRemark(tradeEntity.getRemark());
        tradeDetailWebEntity.setOrderTime(DateFormatUtil.parseToString(tradeEntity.getOrderTime()));
        tradeDetailWebEntity.setTotalMoney(tradeEntity.getTotalAmount());
        tradeDetailWebEntity.setFinalMoney(tradeEntity.getPaymentAmount());
        tradeDetailWebEntity.setSubtractMoney(tradeEntity.getTotalAmount().subtract(tradeEntity.getPaymentAmount()));
    }

    private void fillTradeItem(TradeDetailWebEntity tradeDetailWebEntity, TradeEntity tradeEntity) {
        List<TradeItemEntity> tradeItemEntityList = tradeItemService.findByTradeIdList(Lists.newArrayList(tradeEntity.getId()));
        if (CollectionUtils.isEmpty(tradeItemEntityList)) {
            return;
        }

        tradeDetailWebEntity.setOrderTradeItemList(tradeItemEntityList.stream().map(x -> {
            TradeItemWebEntity tradeItemWebEntity = new TradeItemWebEntity();
            BeanUtil.copyProperties(x, tradeItemWebEntity, false);
            return tradeItemWebEntity;
        }).collect(Collectors.toList()));
    }

    /**
     * 根据条件查询当前用户的订单列表
     *
     * @param tradeConditionWebEntity 条件
     * @return 订单列表
     */
    public ResponsePageEntity<TradeWebEntity> searchUserTradeByPage(TradeConditionWebEntity tradeConditionWebEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        AssertUtil.notNull(currentUserInfo, "请先登录");

        TradeConditionEntity tradeConditionEntity = new TradeConditionEntity();
        BeanUtil.copyProperties(tradeConditionWebEntity, tradeConditionEntity, false);
        //查询全部
        if (Objects.nonNull(tradeConditionEntity.getOrderStatus()) && tradeConditionEntity.getOrderStatus() == 0) {
            tradeConditionEntity.setOrderStatus(null);
        }
        ResponsePageEntity<TradeEntity> responsePageEntity = getSelfProxy().searchByPage(tradeConditionEntity);
        if (CollectionUtils.isEmpty(responsePageEntity.getData())) {
            return ResponsePageEntity.buildEmpty(tradeConditionWebEntity);
        }

        List<TradeWebEntity> tradeWebEntityList = responsePageEntity.getData().stream().map(x -> {
            TradeWebEntity tradeWebEntity = new TradeWebEntity();
            BeanUtil.copyProperties(x, tradeWebEntity, false);
            return tradeWebEntity;
        }).collect(Collectors.toList());

        fillTradeItemWebEntityList(tradeWebEntityList);
        return ResponsePageEntity.build(tradeConditionWebEntity, responsePageEntity.getTotalCount(), tradeWebEntityList);
    }

    private void fillTradeItemWebEntityList(List<TradeWebEntity> tradeWebEntityList) {
        List<Long> tradeIdList = tradeWebEntityList.stream().map(TradeWebEntity::getId).collect(Collectors.toList());
        List<TradeItemEntity> tradeItemEntityList = tradeItemService.findByTradeIdList(tradeIdList);
        if (CollectionUtils.isEmpty(tradeItemEntityList)) {
            return;
        }

        Map<Long, List<TradeItemEntity>> tradeItemMap = tradeItemEntityList.stream().collect(Collectors.groupingBy(TradeItemEntity::getTradeId));
        for (TradeWebEntity tradeWebEntity : tradeWebEntityList) {
            List<TradeItemEntity> tradeItemEntities = tradeItemMap.get(tradeWebEntity.getId());
            if (CollectionUtils.isEmpty(tradeItemEntities)) {
                continue;
            }

            List<TradeItemWebEntity> tradeItemWebEntityList = tradeItemEntities.stream().map(x -> {
                TradeItemWebEntity tradeItemWebEntity = new TradeItemWebEntity();
                BeanUtil.copyProperties(x, tradeItemWebEntity, false);
                return tradeItemWebEntity;
            }).collect(Collectors.toList());
            tradeWebEntity.setTradeItemEntityList(tradeItemWebEntityList);
        }
    }

    /**
     * 确认订单
     *
     * @param tradeConfirmReqWebEntity 确认订单实体
     * @return 订单实体
     */
    public TradeConfirmWebEntity confirm(TradeConfirmReqWebEntity tradeConfirmReqWebEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        AssertUtil.notNull(currentUserInfo, "请先登录");

        List<TradeConfirmItemReqWebEntity> tradeConfirmItemReqWebEntityList = tradeConfirmReqWebEntity.getTradeConfirmItemReqWebEntityList();
        List<Long> shoppingCartIdList = tradeConfirmItemReqWebEntityList.stream()
                .map(TradeConfirmItemReqWebEntity::getShoppingCartId).collect(Collectors.toList());
        ShoppingCartConditionEntity shoppingCartConditionEntity = new ShoppingCartConditionEntity();
        shoppingCartConditionEntity.setIdList(shoppingCartIdList);
        shoppingCartConditionEntity.setPageSize(0);
        List<ShoppingCartEntity> shoppingCartEntities = shoppingCartMapper.searchByCondition(shoppingCartConditionEntity);
        AssertUtil.notEmpty(shoppingCartEntities, "您添加到购物车中的商品不存在，请重新添加");

        List<Long> couponIdList = tradeConfirmItemReqWebEntityList.stream().filter(x -> Objects.nonNull(x.getCouponId()))
                .map(TradeConfirmItemReqWebEntity::getCouponId).collect(Collectors.toList());
        List<CouponEntity> couponEntities = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(couponIdList)) {
            CouponConditionEntity couponConditionEntity = new CouponConditionEntity();
            couponConditionEntity.setIdList(couponIdList);
            couponConditionEntity.setPageSize(0);
            couponEntities = couponMapper.searchByCondition(couponConditionEntity);
            AssertUtil.notEmpty(couponEntities, "您的购物车商品对应的优惠券不存在");
        }

        checkStock(shoppingCartEntities);

        TradeConfirmWebEntity tradeConfirmWebEntity = buildTradeWebEntity(tradeConfirmItemReqWebEntityList, shoppingCartEntities, couponEntities);
        tradeConfirmWebEntity.setTradeCode(OrderCodeUtil.generateOrderCode());
        tradeConfirmWebEntity.setShoppingCartProductEntityList(tradeConfirmReqWebEntity.getTradeConfirmItemReqWebEntityList());

        //将确认订单保存到Redis，防止后续有人不确认订单，直接走下单逻辑
        redisUtil.set(currentUserInfo.getId().toString(), JSONUtil.toJsonPrettyStr(tradeConfirmWebEntity), confirmTradeExpireTime);
        return tradeConfirmWebEntity;
    }

    /**
     * 检查库存
     *
     * @param shoppingCartEntities 购物车实体
     */
    public void checkStock(List<ShoppingCartEntity> shoppingCartEntities) {
        List<TradeItemEntity> tradeItemList = shoppingCartEntities.stream().map(x -> {
            TradeItemEntity tradeItemEntity = new TradeItemEntity();
            tradeItemEntity.setProductId(x.getProductId());
            tradeItemEntity.setQuantity(x.getQuantity());
            return tradeItemEntity;
        }).collect(Collectors.toList());
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setTradeItemEntityList(tradeItemList);

        checkProductAndStock(tradeEntity);
    }

    /**
     * 删除订单
     *
     * @return 订单实体
     */
    public void delete(TradeOperateWebEntity tradeOperateWebEntity) {
        TradeEntity tradeEntity = queryTrade(tradeOperateWebEntity);
        AssertUtil.isTrue(OrderStatusEnum.CANCEL.getValue().equals(tradeEntity.getOrderStatus())
                || OrderStatusEnum.REJECT.getValue().equals(tradeEntity.getOrderStatus()), "该订单无法删除");

        TradeEntity newTradeEntity = new TradeEntity();
        newTradeEntity.setId(tradeEntity.getId());
        newTradeEntity.setIsDel(DelEnum.YES.getValue());
        FillUserUtil.fillUpdateUserInfo(newTradeEntity);
        getSelfProxy().update(newTradeEntity);
    }

    /**
     * 取消订单
     *
     * @return 订单实体
     */
    public void cancel(TradeOperateWebEntity tradeOperateWebEntity) {
        TradeEntity tradeEntity = queryTrade(tradeOperateWebEntity);
        AssertUtil.isTrue(OrderStatusEnum.CREATE.getValue().equals(tradeEntity.getOrderStatus())
                || OrderStatusEnum.PAY.getValue().equals(tradeEntity.getOrderStatus()), "该订单无法取消");

        TradeEntity newTradeEntity = new TradeEntity();
        newTradeEntity.setId(tradeEntity.getId());
        newTradeEntity.setOrderStatus(OrderStatusEnum.CANCEL.getValue());
        FillUserUtil.fillUpdateUserInfo(newTradeEntity);
        getSelfProxy().update(newTradeEntity);
    }

    private TradeEntity queryTrade(TradeOperateWebEntity tradeOperateWebEntity) {
        TradeService tradeServiceProxy = getSelfProxy();
        TradeConditionEntity tradeConditionEntity = new TradeConditionEntity();
        tradeConditionEntity.setCode(tradeOperateWebEntity.getTradeCode());
        List<TradeEntity> tradeEntities = tradeServiceProxy.searchByCondition(tradeConditionEntity);
        AssertUtil.notEmpty(tradeEntities, "该订单不存在");

        return tradeEntities.get(0);
    }

    /**
     * 确认收货
     *
     * @return 订单实体
     */
    public void confirmReceive(TradeOperateWebEntity tradeOperateWebEntity) {
        TradeEntity tradeEntity = queryTrade(tradeOperateWebEntity);
        AssertUtil.isTrue(OrderStatusEnum.SHIPPED.getValue().equals(tradeEntity.getOrderStatus()), "该订单无法确认收货");

        TradeEntity newTradeEntity = new TradeEntity();
        newTradeEntity.setId(tradeEntity.getId());
        newTradeEntity.setOrderStatus(OrderStatusEnum.FINISH.getValue());
        FillUserUtil.fillUpdateUserInfo(newTradeEntity);
        getSelfProxy().update(newTradeEntity);
    }

    /**
     * 提交订单
     *
     * @param tradeSubmitWebEntity 提交订单实体
     * @return 订单实体
     */
    public TradeConfirmWebEntity submit(TradeSubmitWebEntity tradeSubmitWebEntity) {
        TradeConfirmWebEntity tradeConfirmWebEntity = checkParam(tradeSubmitWebEntity);
        DeliveryAddressEntity deliveryAddressEntity = deliveryAddressMapper.findById(tradeSubmitWebEntity.getDeliveryAddressId());
        AssertUtil.notNull(deliveryAddressEntity, "该收货地址在系统中不存在");

        doSubmitOrder(tradeSubmitWebEntity, tradeConfirmWebEntity, deliveryAddressEntity);
        return tradeConfirmWebEntity;
    }

    private TradeConfirmWebEntity checkParam(TradeSubmitWebEntity tradeSubmitWebEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        AssertUtil.notNull(currentUserInfo, "请先登录");

        String jsonStr = redisUtil.get(currentUserInfo.getId().toString());
        AssertUtil.isTrue(StringUtils.hasLength(jsonStr), "订单已过期，请重新确认订单后下单");
        TradeConfirmWebEntity tradeConfirmWebEntity = JSONUtil.toBean(jsonStr, TradeConfirmWebEntity.class);
        AssertUtil.isTrue(tradeConfirmWebEntity.getTradeCode().equals(tradeSubmitWebEntity.getTradeCode()), "订单已过期，请重新确认订单后下单");

        return tradeConfirmWebEntity;
    }


    private TradeEntity doSubmitOrder(TradeSubmitWebEntity tradeSubmitWebEntity,
                                      TradeConfirmWebEntity tradeConfirmWebEntity,
                                      DeliveryAddressEntity deliveryAddressEntity) {
        TradeEntity tradeEntity = convertToTradeEntity(tradeSubmitWebEntity, tradeConfirmWebEntity);
        //检查用户的优惠券
        List<CouponUserReceiveEntity> couponUserReceiveEntities = queryCouponList(tradeConfirmWebEntity);
        //检查订单参数
        checkOrderTrade(tradeEntity);
        //扣减库存
        reduceStock(tradeEntity);
        //下单
        createOrderTrade(tradeEntity);
        //保存订单中的收货地址
        //todo 分布式事务的处理
        saveOrderDeliveryAddress(tradeEntity, deliveryAddressEntity);

        transactionTemplate.execute((status -> {
            //使用优惠券
            if (!CollectionUtils.isEmpty(couponUserReceiveEntities)) {
                couponUserReceiveMapper.updateForBatch(couponUserReceiveEntities);
            }
            //下单成功之后，需要删除购物车商品
            deleteShoppingCartProduct(tradeConfirmWebEntity);
            return Boolean.TRUE;
        }));

        return tradeEntity;
    }

    private TradeEntity reduceStock(TradeEntity tradeEntity) {
        List<String> keys = getLockKey(tradeEntity);
        redissonUtil.tryMultiLock(keys, reduceStockLockSeconds, reduceStockLockSeconds, () -> {
            checkProductAndStock(tradeEntity);

            transactionTemplate.execute(status -> {
                for (TradeItemEntity tradeItemEntity : tradeEntity.getTradeItemEntityList()) {
                    productMapper.reduceStock(tradeItemEntity.getProductId(), tradeItemEntity.getQuantity());
                }
                return Boolean.TRUE;
            });

            return tradeEntity;
        });
        return tradeEntity;
    }

    private void checkProductAndStock(TradeEntity tradeEntity) {
        List<Long> productIdList = tradeEntity.getTradeItemEntityList().stream()
                .map(TradeItemEntity::getProductId).distinct().collect(Collectors.toList());
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setIdList(productIdList);
        productConditionEntity.setPageSize(0);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        AssertUtil.notEmpty(productEntities, "商品不存在");
        List<Long> noFoundList = productIdList.stream()
                .filter(x -> productEntities.stream().noneMatch(p -> p.getId().equals(x)))
                .collect(Collectors.toList());
        AssertUtil.isTrue(CollectionUtils.isEmpty(noFoundList), "部分商品已不存在");

        for (TradeItemEntity tradeItemEntity : tradeEntity.getTradeItemEntityList()) {
            ProductEntity productEntity = productEntities.stream().filter(x -> x.getId().equals(tradeItemEntity.getProductId())).findAny()
                    .orElseThrow(() -> new BusinessException(String.format("商品[%s]不存在", tradeItemEntity.getProductName())));
            //如果下单的商品数量大于剩余库存
            if (tradeItemEntity.getQuantity() > productEntity.getRemainQuantity()) {
                throw new BusinessException(String.format("商品[%s]库存不足", tradeItemEntity.getProductName()));
            }
        }
    }

    private List<String> getLockKey(TradeEntity tradeEntity) {
        return tradeEntity.getTradeItemEntityList().stream().map(x -> String.format("%s%s", REDUCE_STOCK_LOCK_PREFIX, x.getProductId())).collect(Collectors.toList());
    }

    private void saveOrderDeliveryAddress(TradeEntity tradeEntity, DeliveryAddressEntity deliveryAddressEntity) {
        TradeDeliveryAddressEntity tradeDeliveryAddressEntity = new TradeDeliveryAddressEntity();
        BeanUtil.copyProperties(deliveryAddressEntity, tradeDeliveryAddressEntity, false);
        tradeDeliveryAddressEntity.setTradeId(tradeEntity.getId());
        tradeDeliveryAddressEntity.setCode(tradeEntity.getCode());
        tradeDeliveryAddressEntity.setUserId(tradeEntity.getUserId());
        tradeDeliveryAddressEntity.setUserName(tradeEntity.getUserName());
        tradeDeliveryAddressService.insert(tradeDeliveryAddressEntity);
    }

    private List<CouponUserReceiveEntity> queryCouponList(TradeConfirmWebEntity tradeConfirmWebEntity) {
        List<Long> couponIdList = tradeConfirmWebEntity.getShoppingCartProductEntityList().stream().filter(x -> Objects.nonNull(x.getCouponId()))
                .map(TradeConfirmItemReqWebEntity::getCouponId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(couponIdList)) {
            return Collections.emptyList();
        }

        CouponUserReceiveConditionEntity couponUserReceiveConditionEntity = new CouponUserReceiveConditionEntity();
        couponUserReceiveConditionEntity.setCouponIdList(couponIdList);
        couponUserReceiveConditionEntity.setUserId(FillUserUtil.getCurrentUserInfo().getId());
        List<CouponUserReceiveEntity> couponUserReceiveEntities = couponUserReceiveMapper.searchByCondition(couponUserReceiveConditionEntity);
        AssertUtil.notEmpty(couponUserReceiveEntities, "用户没有领取部分优惠券，无法使用");
        List<Long> notFoundList = couponIdList.stream()
                .filter(x -> couponUserReceiveEntities.stream().noneMatch(c -> c.getCouponId().equals(x)))
                .collect(Collectors.toList());
        AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), "用户没有领取部分优惠券，无法使用");
        return couponUserReceiveEntities;
    }

    private void deleteShoppingCartProduct(TradeConfirmWebEntity tradeConfirmWebEntity) {
        List<Long> shoppingCartProductIdList = tradeConfirmWebEntity.getShoppingCartProductEntityList().stream()
                .map(TradeConfirmItemReqWebEntity::getShoppingCartId).collect(Collectors.toList());
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        FillUserUtil.fillUpdateUserInfo(shoppingCartEntity);
        shoppingCartMapper.deleteByIds(shoppingCartProductIdList, shoppingCartEntity);
    }

    private TradeEntity convertToTradeEntity(TradeSubmitWebEntity tradeSubmitWebEntity, TradeConfirmWebEntity tradeConfirmWebEntity) {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setCode(tradeConfirmWebEntity.getTradeCode());
        tradeEntity.setUserId(FillUserUtil.getCurrentUserInfo().getId());
        tradeEntity.setOrderTime(new Date());
        tradeEntity.setOrderStatus(OrderStatusEnum.CREATE.getValue());
        tradeEntity.setPayStatus(PayStatusEnum.WAIT_PAY.getValue());
        tradeEntity.setOrderType(OrderTypeEnum.NORMAL_PRODUCT.getValue());
        tradeEntity.setRemark(tradeSubmitWebEntity.getRemark());
        tradeEntity.setTotalAmount(tradeConfirmWebEntity.getTotalMoney());
        tradeEntity.setPaymentAmount(tradeConfirmWebEntity.getFinalMoney());
        tradeEntity.setTradeItemEntityList(convertToTradeItemEntityList(tradeConfirmWebEntity));
        return tradeEntity;
    }

    private List<TradeItemEntity> convertToTradeItemEntityList(TradeConfirmWebEntity tradeConfirmWebEntity) {
        return tradeConfirmWebEntity.getOrderTradeItemList().stream().map(x -> {
            TradeItemEntity tradeItemEntity = new TradeItemEntity();
            tradeItemEntity.setProductId(x.getProductId());
            tradeItemEntity.setProductName(x.getProductName());
            tradeItemEntity.setModel(x.getModel());
            tradeItemEntity.setPrice(x.getPrice());
            tradeItemEntity.setQuantity(x.getQuantity());
            tradeItemEntity.setAmount(x.getAmount());
            return tradeItemEntity;
        }).collect(Collectors.toList());
    }


    private TradeConfirmWebEntity buildTradeWebEntity(List<TradeConfirmItemReqWebEntity> tradeConfirmItemReqWebEntityList,
                                                      List<ShoppingCartEntity> shoppingCartEntities,
                                                      List<CouponEntity> couponEntities) {
        TradeConfirmWebEntity tradeConfirmWebEntity = new TradeConfirmWebEntity();
        userProductHelper.fillProductInfo(shoppingCartEntities);
        fillDeliveryAddress(tradeConfirmWebEntity);
        buildTradeItemWebEntityList(tradeConfirmWebEntity, shoppingCartEntities);
        calcCouponMoney(tradeConfirmItemReqWebEntityList, tradeConfirmWebEntity, couponEntities);
        fillCouponList(tradeConfirmWebEntity);
        return tradeConfirmWebEntity;
    }

    private void fillDeliveryAddress(TradeConfirmWebEntity tradeConfirmWebEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        DeliveryAddressConditionEntity deliveryAddressConditionEntity = new DeliveryAddressConditionEntity();
        deliveryAddressConditionEntity.setUserId(currentUserInfo.getId());
        deliveryAddressConditionEntity.setIsDefault(1);
        List<DeliveryAddressEntity> deliveryAddressEntities = deliveryAddressMapper.searchByCondition(deliveryAddressConditionEntity);
        if (CollectionUtils.isEmpty(deliveryAddressEntities)) {
            return;
        }

        DeliveryAddressEntity deliveryAddressEntity = deliveryAddressEntities.get(0);
        DeliveryAddressWebEntity deliveryAddressWebEntity = new DeliveryAddressWebEntity();
        BeanUtil.copyProperties(deliveryAddressEntity, deliveryAddressWebEntity, true);
        tradeConfirmWebEntity.setDeliveryAddressWebEntity(deliveryAddressWebEntity);
    }

    private void buildTradeItemWebEntityList(TradeConfirmWebEntity tradeConfirmWebEntity, List<ShoppingCartEntity> shoppingCartEntities) {
        tradeConfirmWebEntity.setOrderTradeItemList(shoppingCartEntities.stream().map(x -> {
            TradeItemWebEntity tradeItemWebEntity = new TradeItemWebEntity();
            tradeItemWebEntity.setId(x.getId());
            tradeItemWebEntity.setProductId(x.getProductId());
            tradeItemWebEntity.setProductName(x.getProductName());
            tradeItemWebEntity.setModel(x.getModel());
            tradeItemWebEntity.setCoverUrl(x.getCoverUrl());
            tradeItemWebEntity.setQuantity(x.getQuantity());
            tradeItemWebEntity.setPrice(x.getPrice());
            return tradeItemWebEntity;
        }).collect(Collectors.toList()));
    }

    private void fillCouponList(TradeConfirmWebEntity tradeConfirmWebEntity) {
        List<CouponWebEntity> userCouponList = couponService.getUserCouponList();
        if (CollectionUtils.isEmpty(userCouponList)) {
            return;
        }

        List<CouponWebEntity> canUseCouponList = filterCanUseCouponList(tradeConfirmWebEntity, userCouponList);
        List<CouponWebEntity> unCanUseCouponList = userCouponList.stream()
                .filter(x -> canUseCouponList.stream().noneMatch(c -> c.getCouponId().equals(x.getCouponId())))
                .collect(Collectors.toList());
        TradeCouponWebEntity tradeCouponWebEntity = new TradeCouponWebEntity();
        tradeCouponWebEntity.setCanUseCouponList(unCanUseCouponList);
        tradeCouponWebEntity.setUnCanUseCouponList(unCanUseCouponList);
        tradeCouponWebEntity.setTotalCount(userCouponList.size());
        tradeConfirmWebEntity.setTradeCouponWebEntity(tradeCouponWebEntity);
    }

    private List<CouponWebEntity> filterCanUseCouponList(TradeConfirmWebEntity tradeConfirmWebEntity,
                                                         List<CouponWebEntity> userCouponList) {

        List<CouponWebEntity> filterTimeCouponList = userCouponList.stream().filter(x -> matchTime(x)).collect(Collectors.toList());
        return getCanUseCouponList(tradeConfirmWebEntity, filterTimeCouponList);
    }

    private boolean matchTime(CouponWebEntity couponWebEntity) {
        Date now = new Date();
        return DateFormatUtil.parseToDate(couponWebEntity.getUseStartTimeStr()).before(now)
                && DateFormatUtil.parseToDate(couponWebEntity.getUseEndTimeStr()).after(now);
    }

    private List<CouponWebEntity> getCanUseCouponList(TradeConfirmWebEntity tradeConfirmWebEntity,
                                                      List<CouponWebEntity> userCouponList) {
        List<Long> couponIdList = userCouponList.stream().map(CouponWebEntity::getId).collect(Collectors.toList());
        CouponUserProvideConditionEntity couponUserProvideConditionEntity = new CouponUserProvideConditionEntity();
        couponUserProvideConditionEntity.setValidStatus(ValidStatusEnum.VALID.getValue());
        couponUserProvideConditionEntity.setCouponIdList(couponIdList);
        List<CouponUserProvideEntity> couponUserProvideEntities = couponUserProvideMapper.searchByCondition(couponUserProvideConditionEntity);
        Set<Long> matchCouponIdSet = Sets.newHashSet();

        for (CouponUserProvideEntity couponUserProvideEntity : couponUserProvideEntities) {
            if (couponUserProvideEntity.getProductId() != 0) {
                boolean anyMatch = tradeConfirmWebEntity.getOrderTradeItemList().stream()
                        .anyMatch(x -> x.getProductId().equals(couponUserProvideEntity.getProductId()));
                if (anyMatch) {
                    matchCouponIdSet.add(couponUserProvideEntity.getCouponId());
                }
            } else {
                matchCouponIdSet.add(couponUserProvideEntity.getCouponId());
            }
        }
        return userCouponList.stream().filter(x -> matchCouponIdSet.stream().anyMatch(m -> m.equals(x.getCouponId()))).collect(Collectors.toList());
    }

    private void calcCouponMoney(List<TradeConfirmItemReqWebEntity> tradeConfirmItemReqWebEntityList,
                                 TradeConfirmWebEntity tradeConfirmWebEntity,
                                 List<CouponEntity> couponEntities) {
        Map<Long, List<CouponEntity>> couponMap = couponEntities.stream().collect(Collectors.groupingBy(CouponEntity::getId));
        for (TradeItemWebEntity tradeItemWebEntity : tradeConfirmWebEntity.getOrderTradeItemList()) {
            tradeItemWebEntity.setPayAmount(tradeItemWebEntity.getAmount());
            Optional<TradeConfirmItemReqWebEntity> confirmItemReqWebEntityOptional = tradeConfirmItemReqWebEntityList.stream()
                    .filter(x -> x.getShoppingCartId().equals(tradeItemWebEntity.getId())).findFirst();
            if (confirmItemReqWebEntityOptional.isPresent()) {
                TradeConfirmItemReqWebEntity tradeConfirmItemReqWebEntity = confirmItemReqWebEntityOptional.get();
                if (Objects.nonNull(tradeConfirmItemReqWebEntity.getCouponId())) {
                    List<CouponEntity> findCouponList = couponMap.get(tradeConfirmItemReqWebEntity.getCouponId());
                    if (!CollectionUtils.isEmpty(findCouponList)) {
                        CouponEntity couponEntity = findCouponList.get(0);
                        CouponWebEntity couponWebEntity = CouponUtil.createCouponWebEntity(couponEntity);
                        BigDecimal calcPayPrice = CouponContext.getInstance().calcPayMoney(tradeItemWebEntity.getPrice(), couponWebEntity);
                        tradeItemWebEntity.setPayPrice(calcPayPrice);
                    }

                }
            }
            tradeItemWebEntity.setAmount(getAmount(tradeItemWebEntity.getPrice(), tradeItemWebEntity.getQuantity()));
            tradeConfirmWebEntity.setTotalCount(tradeConfirmWebEntity.getTotalCount() + tradeItemWebEntity.getQuantity());
            tradeConfirmWebEntity.setTotalMoney(tradeConfirmWebEntity.getTotalMoney().add(tradeItemWebEntity.getAmount()));
            BigDecimal payAmount = getAmount(tradeItemWebEntity.getPayPrice(), tradeItemWebEntity.getQuantity());
            if (CollectionUtils.isEmpty(couponEntities)) {
                tradeConfirmWebEntity.setFinalMoney(tradeConfirmWebEntity.getTotalMoney());
            } else {
                tradeConfirmWebEntity.setFinalMoney(tradeConfirmWebEntity.getFinalMoney().add(payAmount));
            }
        }
        tradeConfirmWebEntity.setSubtractMoney(tradeConfirmWebEntity.getTotalMoney().subtract(tradeConfirmWebEntity.getFinalMoney()));
    }

    private BigDecimal getAmount(BigDecimal price, Integer quantity) {
        if (Objects.isNull(price)) {
            price = BigDecimal.ZERO;
        }
        return price.multiply(new BigDecimal(quantity));
    }

    /**
     * 处理订单过期取消消息
     *
     * @param tradeEntity 订单实体
     */
    public void handleOverTimeCancelTrade(TradeEntity tradeEntity) {
        //获取代理对象
        TradeService tradeProxyService = getSelfProxy();
        TradeEntity tradeEntityFromDB = tradeProxyService.findById(tradeEntity.getId());
        AssertUtil.notNull(tradeEntityFromDB, "订单不存在");

        //如果订单过期了，但还是下单状态，则需要自动取消订单
        if (OrderStatusEnum.CREATE.getValue().equals(tradeEntityFromDB.getOrderStatus())) {
            TradeEntity updateEntity = new TradeEntity();
            updateEntity.setOrderStatus(OrderStatusEnum.CANCEL.getValue());
            updateEntity.setUpdateTime(new Date());
            updateEntity.setId(tradeEntityFromDB.getId());
            tradeProxyService.update(updateEntity);

            if (OrderTypeEnum.SECKILL_PRODUCT.getValue().equals(tradeEntityFromDB.getOrderType())) {
                //通知下游业务系统订单状态已变化
                mqHelper.send(overTimeCancelTradeTopic, tradeEntity);
            }
        }
    }

    private String getTradeStatusChangeKey(Long id) {
        return String.format("%s%s", TRADE_STATUS_CHANGE_ROUTING_KEY_PREFIX, id);
    }


    /**
     * 处理用户秒杀商品订单
     *
     * @param userSeckillProductTradeEntity 用户秒杀商品订单实体
     * @return 用户秒杀商品订单
     */
    public UserSeckillProductTradeEntity createUserSeckillProductTrade(UserSeckillProductTradeEntity userSeckillProductTradeEntity) {
        try {
            AssertUtil.notNull(userSeckillProductTradeEntity.getProductId(), "productId不能为空");
            AssertUtil.isTrue(StringUtils.hasLength(userSeckillProductTradeEntity.getUserName()), "userName不能为空");
            FillUserUtil.setCurrentUser(null, userSeckillProductTradeEntity.getUserName());

            UserConditionEntity userConditionEntity = new UserConditionEntity();
            userConditionEntity.setUserName(userSeckillProductTradeEntity.getUserName());
            List<UserEntity> userEntities = userMapper.searchByCondition(userConditionEntity);
            AssertUtil.notEmpty(userEntities, String.format("该用户[%s]不存在", userSeckillProductTradeEntity.getUserName()));
            UserEntity userEntity = userEntities.get(0);

            ProductEntity productEntity = productMapper.findById(userSeckillProductTradeEntity.getProductId());
            AssertUtil.notNull(productEntity, String.format("订单[%s]不存在", userSeckillProductTradeEntity.getProductId()));

            TradeEntity tradeEntity = createTradeEntity(userSeckillProductTradeEntity, productEntity, userEntity);
            TradeEntity oldTradeEntity = tradeSaveService.checkRepeat(tradeEntity, false);
            if (Objects.nonNull(oldTradeEntity)) {
                fillData(userSeckillProductTradeEntity, userEntity, productEntity, oldTradeEntity);
                tradeEntity.setId(oldTradeEntity.getId());
                return userSeckillProductTradeEntity;
            }

            tradeEntity.setOrderType(OrderTypeEnum.SECKILL_PRODUCT.getValue());
            FillUserUtil.setCurrentUser(userEntity.getId(), userEntity.getUserName());
            tradeSaveService.createTrade(FillUserUtil.getCurrentUserInfo(), tradeEntity);
            fillData(userSeckillProductTradeEntity, userEntity, productEntity, tradeEntity);
        } finally {
            FillUserUtil.clearCurrentUser();
        }

        return userSeckillProductTradeEntity;
    }

    private void fillData(UserSeckillProductTradeEntity userSeckillProductTradeEntity,
                          UserEntity userEntity,
                          ProductEntity productEntity,
                          TradeEntity tradeEntity) {
        userSeckillProductTradeEntity.setCostPrice(productEntity.getPrice());
        userSeckillProductTradeEntity.setProductName(productEntity.getName());
        userSeckillProductTradeEntity.setModel(productEntity.getModel());
        userSeckillProductTradeEntity.setOrderTime(tradeEntity.getOrderTime());
        userSeckillProductTradeEntity.setTradeId(tradeEntity.getId());
        userSeckillProductTradeEntity.setUserId(userEntity.getId());
    }

    private TradeEntity createTradeEntity(UserSeckillProductTradeEntity userSeckillProductTradeEntity, ProductEntity productEntity, UserEntity userEntity) {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setCode(userSeckillProductTradeEntity.getCode());
        tradeEntity.setTotalAmount(userSeckillProductTradeEntity.getTotalAmount());
        tradeEntity.setPaymentAmount(userSeckillProductTradeEntity.getPaymentAmount());
        tradeEntity.setUserName(userSeckillProductTradeEntity.getUserName());
        tradeEntity.setUserId(userEntity.getId());

        TradeItemEntity tradeItemEntity = new TradeItemEntity();
        tradeItemEntity.setCode(userSeckillProductTradeEntity.getCode());
        tradeItemEntity.setProductId(userSeckillProductTradeEntity.getProductId());
        tradeItemEntity.setModel(productEntity.getModel());
        tradeItemEntity.setProductName(productEntity.getName());
        tradeItemEntity.setPrice(userSeckillProductTradeEntity.getPrice());
        tradeItemEntity.setAmount(userSeckillProductTradeEntity.getTotalAmount());
        tradeItemEntity.setQuantity(userSeckillProductTradeEntity.getQuantity());
        tradeEntity.setTradeItemEntityList(Lists.newArrayList(tradeItemEntity));
        return tradeEntity;
    }

    /**
     * 查询订单信息
     *
     * @param id 订单ID
     * @return 订单信息
     */
    @DS("sharding")
    public TradeEntity findById(Long id) {
        return tradeMapper.findById(id);
    }

    /**
     * 根据条件分页查询订单列表
     *
     * @param tradeConditionEntity 订单信息
     * @return 订单集合
     */
    @DS("sharding")
    public ResponsePageEntity<TradeEntity> searchByPage(TradeConditionEntity tradeConditionEntity) {
        return super.searchByPage(tradeConditionEntity);
    }

    /**
     * 根据订单编码查询订单
     *
     * @param code 订单编码
     * @return 订单集合
     */
    @DS("sharding")
    public TradeEntity findByCode(String code) {
        TradeConditionEntity tradeConditionEntity = new TradeConditionEntity();
        tradeConditionEntity.setCode(code);
        List<TradeEntity> tradeEntities = tradeMapper.searchByCondition(tradeConditionEntity);
        if (CollectionUtils.isEmpty(tradeEntities)) {
            return null;
        }

        return tradeEntities.get(0);
    }

    /**
     * 根据条件查询订单列表
     *
     * @param tradeConditionEntity 查询条件
     * @return 订单集合
     */
    @DS("sharding")
    public List<TradeEntity> searchByCondition(TradeConditionEntity tradeConditionEntity) {
        tradeConditionEntity.setPageSize(0);
        return tradeMapper.searchByCondition(tradeConditionEntity);
    }

    /**
     * 根据条件查询订单数量
     *
     * @param tradeConditionEntity 查询条件
     * @return 数量
     */
    @DS("sharding")
    public int searchCount(TradeConditionEntity tradeConditionEntity) {
        return tradeMapper.searchCount(tradeConditionEntity);
    }

    /**
     * 用户下单
     *
     * @param tradeEntity 订单信息
     * @return 结果
     */
    @DS("sharding")
    public void createOrderTrade(TradeEntity tradeEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        checkOrderTrade(tradeEntity);

        tradeEntity.setOrderType(OrderTypeEnum.NORMAL_PRODUCT.getValue());
        fillTradeItemEntity(tradeEntity);

        tradeSaveService.createTrade(currentUserInfo, tradeEntity);
    }

    /**
     * 检查下单参数
     *
     * @param tradeEntity 订单实体
     */
    public void checkOrderTrade(TradeEntity tradeEntity) {
        checkParam(tradeEntity);
        tradeSaveService.checkRepeat(tradeEntity, true);
    }

    private void fillTradeItemEntity(TradeEntity tradeEntity) {
        List<Long> productIdList = tradeEntity.getTradeItemEntityList()
                .stream().map(TradeItemEntity::getProductId).collect(Collectors.toList());

        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setIdList(productIdList);
        productConditionEntity.setPageSize(0);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        AssertUtil.notEmpty(productEntities, "商品不能为空");

        List<Long> notFoundList = productIdList.stream().filter(x -> productEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
        AssertUtil.isTrue(org.apache.commons.collections4.CollectionUtils.isEmpty(notFoundList), String.format("商品ID：%s，在系统中不存在", notFoundList));

        for (TradeItemEntity tradeItemEntity : tradeEntity.getTradeItemEntityList()) {
            ProductEntity productEntity = productEntities.stream().filter(x -> x.getId().equals(tradeItemEntity.getProductId()))
                    .findAny().orElseThrow(() -> new BusinessException(String.format("商品ID：%s，在系统中不存在", tradeItemEntity.getProductId())));
            tradeItemEntity.setProductName(productEntity.getName());
            tradeItemEntity.setModel(productEntity.getModel());
            tradeItemEntity.setId(idGenerateHelper.nextId());
            tradeItemEntity.setCoverUrl(productEntity.getCoverUrl());
        }
    }

    private void checkParam(TradeEntity tradeEntity) {
        AssertUtil.isTrue(StringUtils.hasLength(tradeEntity.getCode()), "订单编码不能为空");
        AssertUtil.isTrue(tradeEntity.getCode().length() == ORDER_CODE_LENGTH, String.format("订单编码要求必须是%s位的", ORDER_CODE_LENGTH));

        AssertUtil.isTrue(checkGtZero(tradeEntity.getTotalAmount()), "总金额必须大于0");
        AssertUtil.isTrue(checkLtZero(tradeEntity.getTotalAmount(), 100000), "总金额必须小于100000");
        AssertUtil.isTrue(checkGtZero(tradeEntity.getPaymentAmount()), "付款金额必须大于0");
        AssertUtil.isTrue(checkLtZero(tradeEntity.getPaymentAmount(), 100000), "总金额必须小于100000");

        for (TradeItemEntity tradeItemEntity : tradeEntity.getTradeItemEntityList()) {
            AssertUtil.isTrue(checkGtZero(tradeItemEntity.getAmount()), "金额必须大于0");
            AssertUtil.isTrue(checkLtZero(tradeItemEntity.getAmount(), 100000), "金额必须小于10000");
            AssertUtil.isTrue(checkGtZero(tradeItemEntity.getPrice()), "单价必须大于0");
            AssertUtil.isTrue(checkLtZero(tradeItemEntity.getPrice(), 100000), "单价必须小于10000");
            AssertUtil.isTrue(tradeItemEntity.getQuantity() > 0, "数量必须大于0");
        }
    }

    private boolean checkGtZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean checkLtZero(BigDecimal value, Integer maxValue) {
        return value.compareTo(new BigDecimal(maxValue)) < 0;
    }


    /**
     * 修改订单
     *
     * @param tradeEntity 订单信息
     * @return 结果
     */
    @DS("sharding")
    public int update(TradeEntity tradeEntity) {
        return tradeMapper.update(tradeEntity);
    }

    /**
     * 批量删除订单对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    @DS("sharding")
    public int deleteByIds(List<Long> ids) {
        List<TradeEntity> entities = tradeMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "订单已被删除");

        TradeEntity entity = new TradeEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return tradeMapper.deleteByIds(ids, entity);
    }

    /**
     * 批量发货
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    @DS("sharding")
    public int shippedByIds(List<Long> ids) {
        List<TradeEntity> entities = tradeMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "订单已被删除");

        for (TradeEntity entity : entities) {
            AssertUtil.isTrue(!OrderStatusEnum.SHIPPED.getValue().equals(entity.getOrderStatus()), "订单已发货");
        }

        TradeEntity entity = new TradeEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return tradeMapper.updateOrderStatusByIds(ids, entity, OrderStatusEnum.SHIPPED.getValue());
    }


    @Override
    protected BaseMapper getBaseMapper() {
        return tradeMapper;
    }

}
