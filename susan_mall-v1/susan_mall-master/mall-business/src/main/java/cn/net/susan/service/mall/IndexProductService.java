package cn.net.susan.service.mall;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.net.susan.entity.mall.IndexCarouselImageEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.ProductPhotoConditionEntity;
import cn.net.susan.entity.mall.ProductPhotoEntity;
import cn.net.susan.entity.seckill.SeckillProductConditionEntity;
import cn.net.susan.entity.seckill.SeckillProductEntity;
import cn.net.susan.enums.ProductTypeEnum;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.mapper.mall.ProductPhotoMapper;
import cn.net.susan.mapper.seckill.SeckillProductMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.mall.IndexProductMapper;
import cn.net.susan.entity.mall.IndexProductConditionEntity;
import cn.net.susan.entity.mall.IndexProductEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;
import org.springframework.util.StringUtils;


import static cn.net.susan.enums.ProductTypeEnum.HOT;
import static cn.net.susan.enums.ProductTypeEnum.NEW;
import static cn.net.susan.enums.ProductTypeEnum.SECKILL;
import static cn.net.susan.util.CoverUtil.getCover;


@Slf4j
@Service
public class IndexProductService extends BaseService<IndexProductEntity, IndexProductConditionEntity> {

    private static final String INDEX_PRODUCT_PREFIX = "indexProduct:";

    @Autowired
    private IndexProductMapper indexProductMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductPhotoMapper productPhotoMapper;
    @Autowired
    private SeckillProductMapper seckillProductMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取首页商品列表
     *
     * @return 首页商品列表
     */
    public List<IndexProductEntity> getIndexProductList(int type) {
        String value = redisUtil.get(getKey(type));
        return JSON.parseArray(value, IndexProductEntity.class);
    }


    /**
     * 刷新首页商品
     *
     * @param productTop 显示多少条数据
     * @param sortParam  排序
     */
    public void refreshIndexProduct(int productTop, String sortParam) {
        for (ProductTypeEnum productTypeEnum : ProductTypeEnum.values()) {
            Integer type = productTypeEnum.getValue();
            IndexProductConditionEntity indexProductConditionEntity = new IndexProductConditionEntity();
            indexProductConditionEntity.setType(type);
            indexProductConditionEntity.setPageNo(1);
            indexProductConditionEntity.setPageSize(productTop);
            if (StringUtils.hasLength(sortParam)) {
                indexProductConditionEntity.setSortField(Arrays.stream(sortParam.split(" ")).collect(Collectors.toList()));
            }
            List<IndexProductEntity> indexProductEntities = indexProductMapper.searchByCondition(indexProductConditionEntity);
            if (CollectionUtils.isEmpty(indexProductEntities)) {
                return;
            }

            fillProductInfo(indexProductEntities);
            redisUtil.set(getKey(type), JSON.toJSONString(indexProductEntities));
        }
        log.info("refreshIndexProduct 更新完成");
    }

    private String getKey(int type) {
        return String.format("%s%s", INDEX_PRODUCT_PREFIX, type);
    }

    /**
     * 查询首页商品信息
     *
     * @param id 首页商品ID
     * @return 首页商品信息
     */
    public IndexProductEntity findById(Long id) {
        return indexProductMapper.findById(id);
    }

    /**
     * 根据条件分页查询首页商品列表
     *
     * @param indexProductConditionEntity 首页商品信息
     * @return 首页商品集合
     */
    public ResponsePageEntity<IndexProductEntity> searchByPage(IndexProductConditionEntity indexProductConditionEntity) {
        ResponsePageEntity<IndexProductEntity> responsePageEntity = super.searchByPage(indexProductConditionEntity);

        if (CollectionUtils.isNotEmpty(responsePageEntity.getData())) {
            fillProductInfo(responsePageEntity.getData());
        }
        return responsePageEntity;
    }

    private void fillProductInfo(List<IndexProductEntity> dataList) {
        List<Long> productIdList = Lists.newArrayList();
        List<Long> hotNewProductIdList = dataList.stream()
                .filter(x -> HOT.getValue().equals(x.getType()) || NEW.getValue().equals(x.getType()))
                .map(IndexProductEntity::getProductId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> seckillProductIdList = dataList.stream()
                .filter(x -> SECKILL.getValue().equals(x.getType()))
                .map(IndexProductEntity::getProductId)
                .distinct()
                .collect(Collectors.toList());

        SeckillProductConditionEntity seckillProductConditionEntity = new SeckillProductConditionEntity();
        seckillProductConditionEntity.setIdList(seckillProductIdList);
        List<SeckillProductEntity> seckillProductEntities = seckillProductMapper.searchByCondition(seckillProductConditionEntity);
        List<Long> moreProductIdList = seckillProductEntities.stream().map(SeckillProductEntity::getProductId).distinct().collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(hotNewProductIdList)) {
            productIdList.addAll(hotNewProductIdList);
        }

        if (CollectionUtils.isNotEmpty(moreProductIdList)) {
            productIdList.addAll(moreProductIdList);
        }

        if (CollectionUtils.isEmpty(productIdList)) {
            return;
        }

        ProductPhotoConditionEntity productPhotoConditionEntity = new ProductPhotoConditionEntity();
        productPhotoConditionEntity.setProductIdList(productIdList);
        productPhotoConditionEntity.setPageSize(0);
        productPhotoConditionEntity.setType(1);
        List<ProductPhotoEntity> productPhotoEntities = productPhotoMapper.searchByCondition(productPhotoConditionEntity);

        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setIdList(hotNewProductIdList);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        for (IndexProductEntity indexProductEntity : dataList) {
            if (HOT.getValue().equals(indexProductEntity.getType()) || NEW.getValue().equals(indexProductEntity.getType())) {
                fillData(indexProductEntity, indexProductEntity.getProductId(), productEntities, productPhotoEntities);
            } else {
                Optional<SeckillProductEntity> seckillProductEntityOptional = seckillProductEntities.stream().filter(x -> x.getId().equals(indexProductEntity.getProductId())).findAny();
                if (seckillProductEntityOptional.isPresent()) {
                    SeckillProductEntity seckillProductEntity = seckillProductEntityOptional.get();
                    fillData(indexProductEntity, seckillProductEntity.getProductId(), productEntities, productPhotoEntities);
                    indexProductEntity.setPrice(seckillProductEntity.getPrice());
                }
            }
        }

    }

    private void fillData(IndexProductEntity indexProductEntity, Long productId, List<ProductEntity> productEntities, List<ProductPhotoEntity> productPhotoEntities) {
        Optional<ProductEntity> productOptional = productEntities.stream().filter(x -> x.getId().equals(productId)).findAny();
        if (productOptional.isPresent()) {
            ProductEntity productEntity = productOptional.get();
            indexProductEntity.setProductName(productEntity.getName());
            indexProductEntity.setModel(productEntity.getModel());
            indexProductEntity.setPrice(productEntity.getPrice());
            indexProductEntity.setCover(getCover(productEntity.getId(), productPhotoEntities));
        }
    }

    /**
     * 新增首页商品
     *
     * @param indexProductEntity 首页商品信息
     * @return 结果
     */
    public int insert(IndexProductEntity indexProductEntity) {
        return indexProductMapper.insert(indexProductEntity);
    }

    /**
     * 修改首页商品
     *
     * @param indexProductEntity 首页商品信息
     * @return 结果
     */
    public int update(IndexProductEntity indexProductEntity) {
        return indexProductMapper.update(indexProductEntity);
    }

    /**
     * 批量删除首页商品对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<IndexProductEntity> entities = indexProductMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "首页商品已被删除");

        IndexProductEntity entity = new IndexProductEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return indexProductMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return indexProductMapper;
    }

}
