package cn.net.susan.service.shopping;

import cn.hutool.core.bean.BeanUtil;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.web.OrderTradeProductCommentWebEntity;
import cn.net.susan.entity.mall.web.ProductCommentWebEntity;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.entity.shopping.ProductCommentConditionEntity;
import cn.net.susan.entity.shopping.ProductCommentEntity;
import cn.net.susan.entity.sys.UserAvatarConditionEntity;
import cn.net.susan.entity.sys.UserAvatarEntity;
import cn.net.susan.entity.sys.UserConditionEntity;
import cn.net.susan.entity.sys.UserEntity;
import cn.net.susan.enums.OrderStatusEnum;
import cn.net.susan.enums.ProductCommentTypeEnum;
import cn.net.susan.helper.IdGenerateHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.mapper.shopping.ProductCommentMapper;
import cn.net.susan.mapper.sys.UserAvatarMapper;
import cn.net.susan.mapper.sys.UserMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.service.order.TradeService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.DateFormatUtil;
import cn.net.susan.util.FillUserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductCommentService extends BaseService<ProductCommentEntity, ProductCommentConditionEntity> {

    @Autowired
    private ProductCommentMapper productCommentMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAvatarMapper userAvatarMapper;
    @Autowired
    private IdGenerateHelper idGenerateHelper;
    @Autowired
    private TradeService tradeService;

    /**
     * 保存的商品用户评价
     *
     * @param orderTradeProductCommentWebEntity 订单商品评价实体
     * @return 影响行数
     */
    public void saveProductComment(OrderTradeProductCommentWebEntity orderTradeProductCommentWebEntity) {
        List<ProductCommentWebEntity> filterList = orderTradeProductCommentWebEntity.getProductCommentWebEntityList()
                .stream().filter(x -> Objects.nonNull(x.getRating())).collect(Collectors.toList());
        AssertUtil.notEmpty(filterList, "请给商品打分");

        TradeEntity tradeEntity = tradeService.findByCode(orderTradeProductCommentWebEntity.getTradeCode());
        AssertUtil.notNull(tradeEntity, "该订单不存在");
        AssertUtil.isTrue(!OrderStatusEnum.COMMENT.getValue().equals(tradeEntity.getOrderStatus()), "该订单已经评价过了");
        AssertUtil.isTrue(OrderStatusEnum.FINISH.getValue().equals(tradeEntity.getOrderStatus()), "该订单不能评价");

        List<ProductCommentEntity> insertList = filterList.stream().map(x -> {
            ProductCommentEntity productCommentEntity = new ProductCommentEntity();
            BeanUtil.copyProperties(x, productCommentEntity, false);
            productCommentEntity.setId(idGenerateHelper.nextId());
            productCommentEntity.setParentId(0L);
            productCommentEntity.setType(getCommentType(x.getRating()));
            productCommentEntity.setUserId(FillUserUtil.getCurrentUserInfo().getId());
            return productCommentEntity;
        }).collect(Collectors.toList());

        productCommentMapper.batchInsert(insertList);

        tradeEntity.setOrderStatus(OrderStatusEnum.COMMENT.getValue());
        FillUserUtil.fillUpdateUserInfo(tradeEntity);
        tradeService.update(tradeEntity);
    }

    private int getCommentType(int rating) {
        if (rating < 2) {
            return ProductCommentTypeEnum.NEGATIVE.getValue();
        } else if (rating >= 2 && rating < 4) {
            return ProductCommentTypeEnum.MODERATE.getValue();
        } else {
            return ProductCommentTypeEnum.POSITIVE.getValue();
        }
    }

    /**
     * 查询商品评论信息
     *
     * @param id 商品评论ID
     * @return 商品评论信息
     */
    public ProductCommentEntity findById(Long id) {
        return productCommentMapper.findById(id);
    }


    /**
     * 根据条件搜索商品评论列表
     *
     * @param productCommentConditionEntity 条件
     * @return 商品评论列表
     */
    public ResponsePageEntity<ProductCommentWebEntity> searchProductComment(ProductCommentConditionEntity productCommentConditionEntity) {
        if (Objects.nonNull(productCommentConditionEntity.getType()) && productCommentConditionEntity.getType() == 0) {
            productCommentConditionEntity.setType(null);
        }
        ResponsePageEntity<ProductCommentEntity> responsePageEntity = super.searchByPage(productCommentConditionEntity);
        if (CollectionUtils.isEmpty(responsePageEntity.getData())) {
            return ResponsePageEntity.buildEmpty(productCommentConditionEntity);
        }

        List<Long> userIdList = responsePageEntity.getData().stream().map(ProductCommentEntity::getUserId).distinct().collect(Collectors.toList());
        UserConditionEntity userConditionEntity = new UserConditionEntity();
        userConditionEntity.setIdList(userIdList);
        List<UserEntity> userEntities = userMapper.searchByCondition(userConditionEntity);

        List<Long> avatarIdList = userEntities.stream().map(UserEntity::getAvatarId).distinct().collect(Collectors.toList());
        UserAvatarConditionEntity userAvatarConditionEntity = new UserAvatarConditionEntity();
        userAvatarConditionEntity.setIdList(avatarIdList);
        List<UserAvatarEntity> userAvatarEntities = userAvatarMapper.searchByCondition(userAvatarConditionEntity);

        List<ProductCommentWebEntity> productCommentWebEntities = responsePageEntity.getData()
                .stream().map(x -> convertProductCommentWebEntity(x, userEntities, userAvatarEntities)).collect(Collectors.toList());
        return ResponsePageEntity.build(productCommentConditionEntity, responsePageEntity.getTotalCount(), productCommentWebEntities);
    }

    private ProductCommentWebEntity convertProductCommentWebEntity(ProductCommentEntity productCommentEntity,
                                                                   List<UserEntity> userEntities,
                                                                   List<UserAvatarEntity> userAvatarEntities) {
        ProductCommentWebEntity productCommentWebEntity = new ProductCommentWebEntity();
        BeanUtil.copyProperties(productCommentEntity, productCommentWebEntity, true);
        productCommentWebEntity.setCreateTimeStr(DateFormatUtil.parseToString(productCommentEntity.getCreateTime()));
        fillUserInfo(productCommentWebEntity, userEntities, userAvatarEntities);
        return productCommentWebEntity;
    }

    private void fillUserInfo(ProductCommentWebEntity productCommentWebEntity,
                              List<UserEntity> userEntities,
                              List<UserAvatarEntity> userAvatarEntities) {
        //填充用户昵称
        Optional<UserEntity> userOptional = userEntities.stream()
                .filter(x -> x.getId().equals(productCommentWebEntity.getUserId())).findFirst();
        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            productCommentWebEntity.setNickName(userEntity.getNickName());

            //填充个人头像
            Optional<UserAvatarEntity> userAvatarEntityOptional = userAvatarEntities.stream()
                    .filter(x -> x.getId().equals(userEntity.getAvatarId())).findFirst();
            if (userAvatarEntityOptional.isPresent()) {
                productCommentWebEntity.setAvatar(userAvatarEntityOptional.get().getPath());
            }
        }
    }

    /**
     * 根据条件分页查询商品评论列表
     *
     * @param productCommentConditionEntity 商品评论信息
     * @return 商品评论集合
     */
    public ResponsePageEntity<ProductCommentEntity> searchByPage(ProductCommentConditionEntity productCommentConditionEntity) {
        return super.searchByPage(productCommentConditionEntity);
    }

    /**
     * 新增商品评论
     *
     * @param productCommentEntity 商品评论信息
     * @return 结果
     */
    public int insert(ProductCommentEntity productCommentEntity) {
        checkParam(productCommentEntity);
        return productCommentMapper.insert(productCommentEntity);
    }

    /**
     * 修改商品评论
     *
     * @param productCommentEntity 商品评论信息
     * @return 结果
     */
    public int update(ProductCommentEntity productCommentEntity) {
        checkParam(productCommentEntity);
        return productCommentMapper.update(productCommentEntity);
    }

    private void checkParam(ProductCommentEntity productCommentEntity) {
        if (Objects.nonNull(productCommentEntity.getParentId()) && productCommentEntity.getParentId() > 0) {
            ProductEntity productEntity = productMapper.findById(productCommentEntity.getParentId());
            AssertUtil.notNull(productEntity, "该父评论在系统中不存在");
        }

        UserEntity userEntity = userMapper.findById(productCommentEntity.getUserId());
        AssertUtil.notNull(userEntity, "该用户在系统中不存在");

        ProductEntity productEntity = productMapper.findById(productCommentEntity.getProductId());
        AssertUtil.notNull(productEntity, "该商品在系统中不存在");
    }

    /**
     * 批量删除商品评论
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<ProductCommentEntity> entities = productCommentMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "商品评论已被删除");

        ProductCommentEntity entity = new ProductCommentEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return productCommentMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return productCommentMapper;
    }
}
