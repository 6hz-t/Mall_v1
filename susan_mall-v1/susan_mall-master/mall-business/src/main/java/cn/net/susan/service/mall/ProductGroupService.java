package cn.net.susan.service.mall;

import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.ProductGroupConditionEntity;
import cn.net.susan.entity.mall.ProductGroupEntity;
import cn.net.susan.helper.CategoryHelper;
import cn.net.susan.helper.UnitHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.mall.ProductGroupMapper;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductGroupService extends BaseService<ProductGroupEntity, ProductGroupConditionEntity> {

    @Autowired
    private ProductGroupMapper productGroupMapper;
    @Autowired
    private CategoryHelper categoryHelper;
    @Autowired
    private UnitHelper unitHelper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询商品组信息
     *
     * @param id 商品组ID
     * @return 商品组信息
     */
    public ProductGroupEntity findById(Long id) {
        return productGroupMapper.findById(id);
    }

    /**
     * 根据条件分页查询商品组列表
     *
     * @param productGroupConditionEntity 商品组信息
     * @return 商品组集合
     */
    public ResponsePageEntity<ProductGroupEntity> searchByPage(ProductGroupConditionEntity productGroupConditionEntity) {
        ResponsePageEntity<ProductGroupEntity> responsePageEntity = super.searchByPage(productGroupConditionEntity);
        if (CollectionUtils.isNotEmpty(responsePageEntity.getData())) {
            categoryHelper.fillCategory(responsePageEntity.getData());
            unitHelper.fillUnit(responsePageEntity.getData());
        }
        return responsePageEntity;
    }

    /**
     * 新增商品组
     *
     * @param productGroupEntity 商品组信息
     * @return 结果
     */
    public int insert(ProductGroupEntity productGroupEntity) {
        return productGroupMapper.insert(productGroupEntity);
    }

    /**
     * 修改商品组
     *
     * @param productGroupEntity 商品组信息
     * @return 结果
     */
    public int update(ProductGroupEntity productGroupEntity) {
        return productGroupMapper.update(productGroupEntity);
    }

    /**
     * 批量删除商品组
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<ProductGroupEntity> entities = productGroupMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "商品组已被删除");

        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setProductGroupIdList(ids);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        AssertUtil.isTrue(CollectionUtils.isEmpty(productEntities), "该商品组下存在商品，请先删除商品");

        ProductGroupEntity entity = new ProductGroupEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return productGroupMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return productGroupMapper;
    }
}
