package cn.net.susan.helper;

import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.UserProductEntity;
import cn.net.susan.entity.sys.UserConditionEntity;
import cn.net.susan.entity.sys.UserEntity;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.mapper.sys.UserMapper;
import cn.net.susan.util.AssertUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.net.susan.util.FillUserUtil.getCurrentUserInfo;


@Component
public class UserProductHelper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 校验参数
     *
     * @param userProductEntity 用户商品实体
     */
    public void checkParam(UserProductEntity userProductEntity) {
        JwtUserEntity currentUserInfo = getCurrentUserInfo();

        ProductEntity productEntity = productMapper.findById(userProductEntity.getProductId());
        AssertUtil.notNull(productEntity, "该商品在系统中不存在");

        userProductEntity.setUserId(currentUserInfo.getId());
    }

    /**
     * 填充用户和商品信息
     *
     * @param list 用户商品实体集合
     */
    public void fillUserProductInfo(List<? extends UserProductEntity> list) {
        fillUserInfo(list);
        fillProductInfo(list);
    }

    /**
     * 填充用户信息
     *
     * @param list 用户集合
     */
    public void fillUserInfo(List<? extends UserProductEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<Long> userIdList = list.stream().map(UserProductEntity::getUserId).distinct().collect(Collectors.toList());
        UserConditionEntity userConditionEntity = new UserConditionEntity();
        userConditionEntity.setIdList(userIdList);
        List<UserEntity> userEntities = userMapper.searchByCondition(userConditionEntity);
        if (CollectionUtils.isNotEmpty(userEntities)) {
            Map<Long, List<UserEntity>> userMap = userEntities.stream().collect(Collectors.groupingBy(UserEntity::getId));
            for (UserProductEntity userProductEntity : list) {
                List<UserEntity> entityList = userMap.get(userProductEntity.getUserId());
                if (CollectionUtils.isNotEmpty(entityList)) {
                    userProductEntity.setUserName(entityList.get(0).getUserName());
                }
            }
        }
    }

    /**
     * 填充商品信息
     *
     * @param list 用户商品实体集合
     */
    public void fillProductInfo(List<? extends UserProductEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<Long> productIdList = list.stream().map(UserProductEntity::getProductId).distinct().collect(Collectors.toList());
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setIdList(productIdList);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);

        if (CollectionUtils.isNotEmpty(productEntities)) {
            Map<Long, List<ProductEntity>> productMap = productEntities.stream().collect(Collectors.groupingBy(ProductEntity::getId));
            for (UserProductEntity userProductEntity : list) {
                List<ProductEntity> entityList = productMap.get(userProductEntity.getProductId());
                if (CollectionUtils.isNotEmpty(entityList)) {
                    ProductEntity productEntity = entityList.get(0);
                    userProductEntity.setModel(productEntity.getModel());
                    userProductEntity.setProductName(productEntity.getName());
                    userProductEntity.setPrice(productEntity.getPrice());
                    userProductEntity.setStock(productEntity.getQuantity());
                    userProductEntity.setCoverUrl(productEntity.getCoverUrl());
                }
            }
        }
    }
}
