package cn.net.susan.helper;

import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.util.AssertUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
public class ProductHelper {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IdGenerateHelper idGenerateHelper;

    /**
     * 批量insert商品
     *
     * @param productEntities 商品列表
     */
    public void batchInsert(List<ProductEntity> productEntities) {
        AssertUtil.notEmpty(productEntities, "商品数据不能为空");

        for (ProductEntity productEntity : productEntities) {
            doInsert(productEntity);
        }
    }

    private void doInsert(ProductEntity productEntity) {
        ProductEntity oldProductEntity = queryOldProductEntity(productEntity);
        if (Objects.nonNull(oldProductEntity)) {
            productEntity.setId(oldProductEntity.getId());
            return;
        }

        try {
            productEntity.setId(idGenerateHelper.nextId());
            productEntity.setProductGroupId(productEntity.getProductGroupEntity().getId());
            productMapper.batchInsert(Lists.newArrayList(productEntity));
            productEntity.setIsNew(true);
        } catch (DuplicateKeyException e) {
            oldProductEntity = queryOldProductEntity(productEntity);
            AssertUtil.notNull(oldProductEntity, "创建商品失败，请稍后重试");
            productEntity.setId(oldProductEntity.getId());
        }
    }


    private ProductEntity queryOldProductEntity(ProductEntity productEntity) {
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setCategoryId(productEntity.getCategoryId());
        productConditionEntity.setProductGroupId(productEntity.getProductGroupEntity().getId());
        productConditionEntity.setUnitId(productEntity.getUnitId());
        productConditionEntity.setBrandId(productEntity.getBrandId());
        productConditionEntity.setHash(productEntity.getHash());
        productConditionEntity.setPageSize(1);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        if (CollectionUtils.isNotEmpty(productEntities)) {
            return productEntities.get(0);
        }
        return null;
    }
}
