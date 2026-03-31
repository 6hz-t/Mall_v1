package cn.net.susan.service.mall;

import cn.hutool.core.bean.BeanUtil;
import cn.net.susan.config.BusinessConfig;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.mall.AttributeConditionEntity;
import cn.net.susan.entity.mall.AttributeEntity;
import cn.net.susan.entity.mall.AttributeValueConditionEntity;
import cn.net.susan.entity.mall.AttributeValueEntity;
import cn.net.susan.entity.mall.BrandConditionEntity;
import cn.net.susan.entity.mall.BrandEntity;
import cn.net.susan.entity.mall.CategoryConditionEntity;
import cn.net.susan.entity.mall.CategoryEntity;
import cn.net.susan.entity.mall.ProductAttributeConditionEntity;
import cn.net.susan.entity.mall.ProductAttributeEntity;
import cn.net.susan.entity.mall.ProductCheckEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductDetailConditionEntity;
import cn.net.susan.entity.mongo.ProductDetailEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.ProductGroupAttributeConditionEntity;
import cn.net.susan.entity.mall.ProductGroupAttributeEntity;
import cn.net.susan.entity.mall.ProductGroupConditionEntity;
import cn.net.susan.entity.mall.ProductGroupEntity;
import cn.net.susan.entity.mall.ProductPhotoConditionEntity;
import cn.net.susan.entity.mall.ProductPhotoEntity;
import cn.net.susan.entity.mall.UnitConditionEntity;
import cn.net.susan.entity.mall.UnitEntity;
import cn.net.susan.entity.mall.web.ProductCommentStatisticWebEntity;
import cn.net.susan.entity.mall.web.ProductConditionWebEntity;
import cn.net.susan.entity.mall.web.ProductDetailWebEntity;
import cn.net.susan.entity.mall.web.ProductGroupAttributeValueWebEntity;
import cn.net.susan.entity.mall.web.ProductGroupAttributeWebEntity;
import cn.net.susan.entity.mall.web.ProductWebEntity;
import cn.net.susan.entity.shopping.ProductCommentConditionEntity;
import cn.net.susan.entity.shopping.ProductFavoritesConditionEntity;
import cn.net.susan.entity.shopping.ProductFavoritesEntity;
import cn.net.susan.entity.shopping.ProductViewRecordConditionEntity;
import cn.net.susan.entity.shopping.ProductViewRecordEntity;
import cn.net.susan.entity.shopping.ShoppingCartConditionEntity;
import cn.net.susan.enums.PhotoTypeEnum;
import cn.net.susan.enums.ProductCommentTypeEnum;
import cn.net.susan.es.EsTemplate;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.helper.BrandHelper;
import cn.net.susan.helper.CategoryHelper;
import cn.net.susan.helper.IdGenerateHelper;
import cn.net.susan.helper.ProductGroupHelper;
import cn.net.susan.helper.ProductHelper;
import cn.net.susan.helper.UnitHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.mall.AttributeMapper;
import cn.net.susan.mapper.mall.AttributeValueMapper;
import cn.net.susan.mapper.mall.BrandMapper;
import cn.net.susan.mapper.mall.CategoryMapper;
import cn.net.susan.mapper.mall.ProductAttributeMapper;
import cn.net.susan.mapper.mall.ProductDetailMapper;
import cn.net.susan.mapper.mall.ProductGroupAttributeMapper;
import cn.net.susan.mapper.mall.ProductGroupMapper;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.mapper.mall.ProductPhotoMapper;
import cn.net.susan.mapper.mall.UnitMapper;
import cn.net.susan.mapper.shopping.ProductCommentMapper;
import cn.net.susan.mapper.shopping.ProductFavoritesMapper;
import cn.net.susan.mapper.shopping.ProductViewRecordMapper;
import cn.net.susan.mapper.shopping.ShoppingCartMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.AttributeUtil;
import cn.net.susan.util.BigDecimalUtil;
import cn.net.susan.util.FillUserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static cn.net.susan.constant.NumberConstant.NUMBER_100;
import static cn.net.susan.constant.NumberConstant.NUMBER_10000;


@Slf4j
@Service
public class ProductService extends BaseService<ProductEntity, ProductConditionEntity> {
    private static final String DEFAULT_SPU_MODEL_HASH = "1000";

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private UnitMapper unitMapper;
    @Autowired
    private AttributeMapper attributeMapper;
    @Autowired
    private AttributeValueMapper attributeValueMapper;
    @Autowired
    private ProductPhotoMapper productPhotoMapper;
    @Autowired
    private ProductAttributeMapper productAttributeMapper;
    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private ProductHelper productHelper;
    @Autowired
    private EsTemplate esTemplate;
    @Autowired
    private BusinessConfig businessConfig;
    @Autowired
    private IdGenerateHelper idGenerateHelper;
    @Autowired
    private ProductFavoritesMapper productFavoritesMapper;
    @Autowired
    private ProductCommentMapper productCommentMapper;
    @Autowired
    private ProductGroupMapper productGroupMapper;
    @Autowired
    private ProductGroupHelper productGroupHelper;
    @Autowired
    private ProductGroupAttributeMapper productGroupAttributeMapper;
    @Autowired
    private CategoryHelper categoryHelper;
    @Autowired
    private BrandHelper brandHelper;
    @Autowired
    private UnitHelper unitHelper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProductViewRecordMapper productViewRecordMapper;
    @Autowired
    private ThreadPoolExecutor productDetailThreadPoolExecutor;

    /**
     * 获取商品详情
     *
     * @param productId 商品ID
     * @return 商品详情
     */
    public ProductDetailWebEntity getDetail(Long productId) {
        ProductDetailWebEntity productDetailWebEntity = new ProductDetailWebEntity();
        ProductEntity productEntity = this.findById(productId);
        BeanUtil.copyProperties(productEntity, productDetailWebEntity, false);

        //填充收藏数据
        fillFavorites(productDetailWebEntity);
        //填充商品评价统计数据
        fillProductCommentStatistic(productDetailWebEntity);
        //填充商品组属性列表
        fillProductGroupAttribute(productEntity, productDetailWebEntity);
        //填充购物车中商品数量
        fillTotalCartNum(productDetailWebEntity);

        //保存用来浏览记录
        JwtUserEntity userEntity = FillUserUtil.getCurrentUserInfoOrNull();
         CompletableFuture.runAsync(() -> {
                    FillUserUtil.mockUser(() -> {
                        saveViewRecord(userEntity, productId);
                        return null;
                    }, userEntity);
                }
                , productDetailThreadPoolExecutor);
        return productDetailWebEntity;
    }


    private void saveViewRecord(JwtUserEntity jwtUserEntity, Long productId) {
        if (Objects.isNull(jwtUserEntity)) {
            return;
        }

        ProductViewRecordConditionEntity productViewRecordConditionEntity = new ProductViewRecordConditionEntity();
        productViewRecordConditionEntity.setProductId(productId);
        productViewRecordConditionEntity.setUserId(jwtUserEntity.getId());
        List<ProductViewRecordEntity> productViewRecordEntities = productViewRecordMapper.searchByCondition(productViewRecordConditionEntity);
        if (CollectionUtils.isNotEmpty(productViewRecordEntities)) {
            ProductViewRecordEntity productViewRecordEntity = productViewRecordEntities.get(0);
            //更新修改时间
            FillUserUtil.fillUpdateUserInfo(productViewRecordEntity);
            productViewRecordEntity.setViewCount(productViewRecordEntity.getViewCount() + 1);
            productViewRecordMapper.update(productViewRecordEntity);
        } else {
            ProductViewRecordEntity productViewRecordEntity = new ProductViewRecordEntity();
            productViewRecordEntity.setProductId(productId);
            productViewRecordEntity.setUserId(jwtUserEntity.getId());
            FillUserUtil.fillUpdateUserInfo(productViewRecordEntity);
            productViewRecordMapper.insert(productViewRecordEntity);
        }
    }

    private void fillFavorites(ProductDetailWebEntity productDetailWebEntity) {
        ProductFavoritesConditionEntity productFavoritesConditionEntity = new ProductFavoritesConditionEntity();
        productFavoritesConditionEntity.setProductId(productDetailWebEntity.getId());
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        if (Objects.isNull(currentUserInfo)) {
            return;
        }
        productFavoritesConditionEntity.setUserId(currentUserInfo.getId());
        productFavoritesConditionEntity.setIsDel(0);
        List<ProductFavoritesEntity> productFavoritesEntities = productFavoritesMapper.searchByCondition(productFavoritesConditionEntity);
        productDetailWebEntity.setIsFavorites(CollectionUtils.isNotEmpty(productFavoritesEntities));
    }


    private void fillProductCommentStatistic(ProductDetailWebEntity productDetailWebEntity) {
        ProductCommentStatisticWebEntity productCommentStatisticWebEntity = new ProductCommentStatisticWebEntity();

        int all = 0;
        int positive = 0;
        ProductCommentConditionEntity productCommentConditionEntity = new ProductCommentConditionEntity();
        productCommentConditionEntity.setProductId(productDetailWebEntity.getId());

        for (ProductCommentTypeEnum productCommentTypeEnum : ProductCommentTypeEnum.values()) {
            productCommentConditionEntity.setType(productCommentTypeEnum.getValue());
            int count = productCommentMapper.searchCount(productCommentConditionEntity);

            switch (productCommentTypeEnum) {
                case POSITIVE:
                    positive = count;
                    productCommentStatisticWebEntity.setPositive(getStringValue(count));
                    all += count;
                    break;
                case MODERATE:
                    productCommentStatisticWebEntity.setModerate(getStringValue(count));
                    all += count;
                    break;
                case NEGATIVE:
                    productCommentStatisticWebEntity.setNegative(getStringValue(count));
                    all += count;
                    break;
                default:
                    break;
            }
        }

        productCommentStatisticWebEntity.setAll(getStringValue(all));
        if (all > 0) {
            productCommentStatisticWebEntity.setPositiveRating(BigDecimalUtil.roundToString(new BigDecimal((positive * NUMBER_100) / all), 0));
        }
        productDetailWebEntity.setProductCommentStatistic(productCommentStatisticWebEntity);
    }


    private void fillProductGroupAttribute(ProductEntity productEntity, ProductDetailWebEntity productDetailWebEntity) {
        fillSkuAttributeValue(productEntity);
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setProductGroupId(productEntity.getProductGroupId());
        productConditionEntity.setPageSize(0);
        List<ProductEntity> productEntities = productMapper.searchByCondition(productConditionEntity);
        productDetailWebEntity.setGroupProductList(productEntities);

        if (CollectionUtils.isEmpty(productEntities)) {
            return;
        }

        ProductAttributeConditionEntity productAttributeConditionEntity = new ProductAttributeConditionEntity();
        productAttributeConditionEntity.setProductIdList(productEntities.stream().map(ProductEntity::getId).collect(Collectors.toList()));
        productAttributeConditionEntity.setPageSize(0);
        List<ProductAttributeEntity> productAttributeEntities = productAttributeMapper.searchByCondition(productAttributeConditionEntity);
        if (CollectionUtils.isEmpty(productAttributeEntities)) {
            return;
        }

        List<Long> attributeIdList = productAttributeEntities.stream().map(ProductAttributeEntity::getAttributeId).distinct().collect(Collectors.toList());
        List<Long> attributeValueIdList = productAttributeEntities.stream().map(ProductAttributeEntity::getAttributeValueId).distinct().collect(Collectors.toList());

        List<AttributeValueEntity> attributeValueList = getAttributeValueList(attributeIdList, attributeValueIdList);
        if (CollectionUtils.isEmpty(attributeValueList)) {
            return;
        }

        Map<Long, List<AttributeValueEntity>> valueMap = attributeValueList.stream()
                .collect(Collectors.groupingBy(AttributeValueEntity::getAttributeId));
        List<Long> keyList = valueMap.keySet().stream().collect(Collectors.toList());
        keyList.sort((a, b) -> a.compareTo(b));
        List<ProductGroupAttributeWebEntity> groupAttributeWebEntities = Lists.newArrayList();
        for (Long key : keyList) {
            ProductGroupAttributeWebEntity productGroupAttributeWebEntity = new ProductGroupAttributeWebEntity();
            List<AttributeValueEntity> attributeValueEntities = valueMap.get(key);
            AttributeValueEntity attributeValueEntity = attributeValueEntities.get(0);
            productGroupAttributeWebEntity.setId(attributeValueEntity.getAttributeId());
            productGroupAttributeWebEntity.setName(attributeValueEntity.getAttributeName());

            productGroupAttributeWebEntity.setValueList(Lists.newArrayList());

            attributeValueEntities.stream().sorted((a, b) -> a.getSort().compareTo(b.getSort()));
            for (AttributeValueEntity valueEntity : attributeValueEntities) {
                ProductGroupAttributeValueWebEntity productGroupAttributeValueWebEntity = new ProductGroupAttributeValueWebEntity();
                productGroupAttributeValueWebEntity.setId(valueEntity.getId());
                productGroupAttributeValueWebEntity.setValue(valueEntity.getValue());
                productGroupAttributeWebEntity.getValueList().add(productGroupAttributeValueWebEntity);
            }
            groupAttributeWebEntities.add(productGroupAttributeWebEntity);
        }

        productDetailWebEntity.setGroupAttributeList(groupAttributeWebEntities);
    }


    private void fillTotalCartNum(ProductDetailWebEntity productDetailWebEntity) {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfoOrNull();
        if (Objects.isNull(currentUserInfo)) {
            return;
        }
        ShoppingCartConditionEntity shoppingCartConditionEntity = new ShoppingCartConditionEntity();
        shoppingCartConditionEntity.setUserId(currentUserInfo.getId());
        int count = shoppingCartMapper.searchCount(shoppingCartConditionEntity);
        productDetailWebEntity.setTotalCartNum(count);
    }

    private String getStringValue(int count) {
        if (count < NUMBER_10000) {
            return String.valueOf(count);
        }
        return BigDecimalUtil.roundToString(new BigDecimal(count / NUMBER_10000), 1) + "w";
    }

    /**
     * 查询商品信息
     *
     * @param id 商品ID
     * @return 商品信息
     */
    public ProductEntity findById(Long id) {
        ProductEntity productEntity = productMapper.findById(id);
        if (Objects.isNull(productEntity)) {
            return new ProductEntity();
        }
        fillSpuAttributeValue(productEntity);
        fillSkuAttributeValue(productEntity);
        fillPhoto(productEntity);
        fillDetail(productEntity);
        return productEntity;
    }

    private void fillSpuAttributeValue(ProductEntity productEntity) {
        ProductGroupAttributeConditionEntity productGroupAttributeConditionEntity = new ProductGroupAttributeConditionEntity();
        productGroupAttributeConditionEntity.setProductGroupId(productEntity.getProductGroupId());
        List<ProductGroupAttributeEntity> productGroupAttributeEntities = productGroupAttributeMapper.searchByCondition(productGroupAttributeConditionEntity);

        List<Long> attributeValueIdList = productGroupAttributeEntities.stream().map(ProductGroupAttributeEntity::getAttributeValueId).distinct().collect(Collectors.toList());
        List<Long> attributeIdList = productGroupAttributeEntities.stream().map(ProductGroupAttributeEntity::getAttributeId).distinct().collect(Collectors.toList());

        productEntity.setSpuAttributeEntityList(getAttributeValueList(attributeIdList, attributeValueIdList));
    }


    private void fillSkuAttributeValue(ProductEntity productEntity) {
        ProductAttributeConditionEntity productAttributeConditionEntity = new ProductAttributeConditionEntity();
        productAttributeConditionEntity.setProductId(productEntity.getId());
        List<ProductAttributeEntity> productAttributeEntities = productAttributeMapper.searchByCondition(productAttributeConditionEntity);

        List<Long> attributeIdList = productAttributeEntities.stream().map(ProductAttributeEntity::getAttributeId).distinct().collect(Collectors.toList());
        List<Long> attributeValueIdList = productAttributeEntities.stream().map(ProductAttributeEntity::getAttributeValueId).distinct().collect(Collectors.toList());

        productEntity.setSkuAttributeEntityList(getAttributeValueList(attributeIdList, attributeValueIdList));
    }

    private List<AttributeValueEntity> getAttributeValueList(List<Long> attributeIdList, List<Long> attributeValueIdList) {
        AttributeValueConditionEntity attributeValueConditionEntity = new AttributeValueConditionEntity();
        attributeValueConditionEntity.setIdList(attributeValueIdList);
        List<AttributeValueEntity> attributeValueEntities = attributeValueMapper.searchByCondition(attributeValueConditionEntity);

        AttributeConditionEntity attributeConditionEntity = new AttributeConditionEntity();
        attributeConditionEntity.setIdList(attributeIdList);
        List<AttributeEntity> attributeEntities = attributeMapper.searchByCondition(attributeConditionEntity);
        for (AttributeValueEntity attributeValueEntity : attributeValueEntities) {
            Optional<AttributeEntity> attributeEntityOptional = attributeEntities.stream().filter(x -> x.getId().equals(attributeValueEntity.getAttributeId())).findAny();
            if (attributeEntityOptional.isPresent()) {
                attributeValueEntity.setAttributeName(attributeEntityOptional.get().getName());
            }
        }
        return attributeValueEntities;
    }

    private void fillPhoto(ProductEntity productEntity) {
        ProductPhotoConditionEntity productPhotoConditionEntity = new ProductPhotoConditionEntity();
        productPhotoConditionEntity.setProductId(productEntity.getId());
        List<ProductPhotoEntity> productPhotoEntities = productPhotoMapper.searchByCondition(productPhotoConditionEntity);
        if (CollectionUtils.isEmpty(productPhotoEntities)) {
            return;
        }

        Optional<ProductPhotoEntity> photoEntityOptional = productPhotoEntities.stream().filter(x -> PhotoTypeEnum.COVER.getValue().equals(x.getType())).findAny();
        if (photoEntityOptional.isPresent()) {
            productEntity.setCover(Lists.newArrayList(photoEntityOptional.get().getUrl()));
        }

        List<String> swiper = productPhotoEntities.stream().filter(x -> PhotoTypeEnum.SWIPER.getValue().equals(x.getType())).map(m -> m.getUrl()).collect(Collectors.toList());
        productEntity.setSwiper(swiper);
    }

    private void fillDetail(ProductEntity productEntity) {
        Query query = new Query(Criteria.where("productId").is(productEntity.getId()));
        List<ProductDetailEntity> productDetailEntities = mongoTemplate.find(query, ProductDetailEntity.class);
        if (CollectionUtils.isEmpty(productDetailEntities)) {
            return;
        }

        ProductDetailEntity productDetailEntity = productDetailEntities.get(0);
        productEntity.setDetail(productDetailEntity.getDetail());
    }

    /**
     * 根据条件分页查询商品列表
     *
     * @param productConditionEntity 商品信息
     * @return 商品集合
     */
    public ResponsePageEntity<ProductEntity> searchByPage(ProductConditionEntity productConditionEntity) {
        ResponsePageEntity<ProductEntity> responsePageEntity = super.searchByPage(productConditionEntity);
        if (CollectionUtils.isNotEmpty(responsePageEntity.getData())) {
            categoryHelper.fillCategory(responsePageEntity.getData());
            brandHelper.fillBrand(responsePageEntity.getData());
            unitHelper.fillUnit(responsePageEntity.getData());
        }
        return responsePageEntity;
    }


    /**
     * 根据条件分页搜索商品列表
     *
     * @param productConditionEntity 商品信息
     * @return 商品集合
     */
    public ResponsePageEntity<ProductWebEntity> searchFromES(ProductConditionWebEntity productConditionEntity) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(productConditionEntity.getPageBegin());
            searchSourceBuilder.size(productConditionEntity.getPageSize());
            if (Objects.nonNull(productConditionEntity.getCategoryId())) {
                searchSourceBuilder.query(QueryBuilders.matchQuery("categoryId", productConditionEntity.getCategoryId()));
            }
            if (StringUtils.hasLength(productConditionEntity.getKeyword())) {
                searchSourceBuilder.query(QueryBuilders.multiMatchQuery(productConditionEntity.getKeyword(), "name", "model"));
            }
            setTypeCondition(productConditionEntity, searchSourceBuilder);
            log.info("searchFromES请求参数:", searchSourceBuilder);
            ResponsePageEntity responsePageEntity = ResponsePageEntity.buildEmpty(productConditionEntity);
            List<ProductWebEntity> productEntities = esTemplate.search(businessConfig.getProductEsIndexName(), searchSourceBuilder, ProductWebEntity.class, responsePageEntity);
            if (CollectionUtils.isEmpty(productEntities)) {
                return ResponsePageEntity.buildEmpty(productConditionEntity);
            }
            return ResponsePageEntity.build(productConditionEntity, responsePageEntity.getTotalCount(), productEntities);
        } catch (IOException e) {
            log.error("从ES中查询商品失败，原因：", e);
            return ResponsePageEntity.buildEmpty(productConditionEntity);
        }
    }

    private void setTypeCondition(ProductConditionWebEntity productConditionEntity, SearchSourceBuilder searchSourceBuilder) {
        if (Objects.isNull(productConditionEntity.getType())) {
            return;
        }

        switch (productConditionEntity.getType()) {
            case 1:
                sortByComprehensive(searchSourceBuilder);
                break;
            case 2:
                sortBySaleQuantity(searchSourceBuilder);
                break;
            case 3:
                sortByPrice(searchSourceBuilder);
                break;
            default:
                return;
        }
    }

    private void sortByComprehensive(SearchSourceBuilder searchSourceBuilder) {
        searchSourceBuilder.sort(SortBuilders.fieldSort("saleQuantity.keyword").order(SortOrder.DESC));
        searchSourceBuilder.sort(SortBuilders.fieldSort("positiveRating.keyword").order(SortOrder.DESC));
        searchSourceBuilder.sort(SortBuilders.fieldSort("price.keyword").order(SortOrder.DESC));
    }

    private void sortBySaleQuantity(SearchSourceBuilder searchSourceBuilder) {
        searchSourceBuilder.sort(SortBuilders.fieldSort("saleQuantity.keyword").order(SortOrder.DESC));
    }

    private void sortByPrice(SearchSourceBuilder searchSourceBuilder) {
        searchSourceBuilder.sort(SortBuilders.fieldSort("price.keyword").order(SortOrder.DESC));
    }

    /**
     * 批量创建商品
     *
     * @param productEntityList 商品信息
     * @return 结果
     */
    public List<ProductEntity> generate(List<ProductEntity> productEntityList) {
        final ProductCheckEntity productCheckEntity = new ProductCheckEntity();
        checkParams(productEntityList, productCheckEntity);
        doGenerate(productEntityList, productCheckEntity);
        return productEntityList;
    }

    private void checkParams(List<ProductEntity> productEntityList, ProductCheckEntity productCheckEntity) {
        checkCategory(productEntityList, productCheckEntity);
        checkUnit(productEntityList);
        checkBrand(productEntityList, productCheckEntity);
        checkAttribute(productEntityList);
    }

    private void doGenerate(List<ProductEntity> productEntityList, ProductCheckEntity productCheckEntity) {
        List<ProductGroupEntity> productGroupEntityList = Lists.newArrayList();
        Set<String> productGroupSet = Sets.newHashSet();

        for (ProductEntity productEntity : productEntityList) {
            productEntity.setRemainQuantity(productEntity.getQuantity());
            ProductGroupEntity productGroupEntity = createProductGroupEntity(productEntity, productCheckEntity);
            productEntity.setProductGroupEntity(productGroupEntity);

            String productGroupKey = getProductGroupKey(productGroupEntity);
            if (!productGroupSet.contains(productGroupKey)) {
                productGroupSet.add(productGroupKey);
                productGroupEntityList.add(productGroupEntity);
            }

            fillSkuNameAndModel(productEntity, productCheckEntity);
        }

        ProductGroupConditionEntity productGroupConditionEntity = new ProductGroupConditionEntity();
        productGroupConditionEntity.setProductGroupEntities(productGroupEntityList);
        List<ProductGroupEntity> productGroupEntities = productGroupMapper.searchByCondition(productGroupConditionEntity);
        List<ProductGroupEntity> addProductGroupList = productGroupEntityList.stream()
                .filter(x -> productGroupEntities.stream().noneMatch(p -> getProductGroupKey(p).equals(getProductGroupKey(x))))
                .collect(Collectors.toList());
        fillProductGroupExistId(productGroupEntityList, productGroupEntities);

        if (CollectionUtils.isNotEmpty(productEntityList)) {
            transactionTemplate.execute((status -> {
                if (CollectionUtils.isNotEmpty(addProductGroupList)) {
                    productGroupHelper.batchInsert(addProductGroupList);
                }
                productHelper.batchInsert(productEntityList);
                //真正新增的商品
                List<ProductEntity> realAddList = productEntityList.stream().filter(x -> x.getIsNew()).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(realAddList)) {
                    saveProductDetail(realAddList);
                    saveProductGroupAttribute(addProductGroupList, realAddList);
                    saveProductAttribute(realAddList);
                    savePhoto(realAddList);
                }
                return Boolean.TRUE;
            }));
        }
    }

    private String getProductGroupKey(ProductGroupEntity productGroupEntity) {
        return String.format("%s%s%s", productGroupEntity.getCategoryId(), productGroupEntity.getUnitId(), productGroupEntity.getHash());
    }

    private ProductGroupEntity createProductGroupEntity(ProductEntity productEntity, ProductCheckEntity productCheckEntity) {
        CategoryEntity categoryEntity = productCheckEntity.getCategoryEntities().stream().filter(x -> x.getId().equals(productEntity.getCategoryId()))
                .findAny()
                .orElseThrow(() -> new BusinessException(String.format("分类ID%s在系统中不存在", productEntity.getCategoryId())));

        ProductGroupEntity productGroupEntity = new ProductGroupEntity();
        productGroupEntity.setCategoryId(categoryEntity.getId());
        productGroupEntity.setUnitId(productEntity.getUnitId());
        if (CollectionUtils.isNotEmpty(productEntity.getSpuAttributeEntityList())) {
            String name = AttributeUtil.getName(productEntity.getSpuAttributeEntityList());
            String spuModel = AttributeUtil.getModel(productEntity.getSpuAttributeEntityList());
            productGroupEntity.setModel(spuModel);

            String spuModelHash = AttributeUtil.getModelHash(productEntity.getSpuAttributeEntityList());
            productGroupEntity.setHash(spuModelHash);
            productGroupEntity.setName(getSpuName(categoryEntity, name));
        } else {
            productGroupEntity.setHash(DEFAULT_SPU_MODEL_HASH);
            productGroupEntity.setName(getSpuName(categoryEntity, ""));
        }
        return productGroupEntity;
    }

    private String getSpuName(CategoryEntity categoryEntity, String name) {
        return String.format("%s %s", categoryEntity.getName(), name).trim();
    }

    private void deleteProductDetail(ProductEntity productEntity) {
        ProductDetailConditionEntity productDetailConditionEntity = new ProductDetailConditionEntity();
        productDetailConditionEntity.setProductId(productEntity.getId());
        List<ProductDetailEntity> productDetailEntities = productDetailMapper.searchByCondition(productDetailConditionEntity);
        if (CollectionUtils.isNotEmpty(productDetailEntities)) {
            List<Long> idList = productDetailEntities.stream().map(ProductDetailEntity::getId).collect(Collectors.toList());
            ProductDetailEntity deleteEntity = new ProductDetailEntity();
            FillUserUtil.fillUpdateUserInfo(deleteEntity);
            productDetailMapper.deleteByIds(idList, deleteEntity);
        }
    }

    private void deleteProductAttribute(ProductEntity productEntity) {
        ProductAttributeConditionEntity productAttributeConditionEntity = new ProductAttributeConditionEntity();
        productAttributeConditionEntity.setProductId(productEntity.getId());
        List<ProductAttributeEntity> productAttributeEntities = productAttributeMapper.searchByCondition(productAttributeConditionEntity);
        if (CollectionUtils.isNotEmpty(productAttributeEntities)) {
            List<Long> idList = productAttributeEntities.stream().map(ProductAttributeEntity::getId).collect(Collectors.toList());
            ProductAttributeEntity deleteEntity = new ProductAttributeEntity();
            FillUserUtil.fillUpdateUserInfo(deleteEntity);
            productAttributeMapper.deleteByIds(idList, deleteEntity);
        }
    }


    private void deleteProductPhoto(ProductEntity productEntity) {
        ProductPhotoConditionEntity productPhotoConditionEntity = new ProductPhotoConditionEntity();
        productPhotoConditionEntity.setProductId(productEntity.getId());
        List<ProductPhotoEntity> productPhotoEntities = productPhotoMapper.searchByCondition(productPhotoConditionEntity);
        if (CollectionUtils.isNotEmpty(productPhotoEntities)) {
            List<Long> idList = productPhotoEntities.stream().map(ProductPhotoEntity::getId).collect(Collectors.toList());
            ProductPhotoEntity deleteEntity = new ProductPhotoEntity();
            FillUserUtil.fillUpdateUserInfo(deleteEntity);
            productPhotoMapper.deleteByIds(idList, deleteEntity);
        }
    }

    private void fillSkuNameAndModel(ProductEntity productEntity, ProductCheckEntity productCheckEntity) {
        String modelHash = AttributeUtil.getModelHash(productEntity.getSkuAttributeEntityList());
        productEntity.setHash(modelHash);

        if (!StringUtils.hasLength(productEntity.getModel())) {
            String model = AttributeUtil.getModel(productEntity.getSkuAttributeEntityList());
            productEntity.setModel(model);
        }

        if (!StringUtils.hasLength(productEntity.getName())) {
            String productName = getProductName(productEntity, productCheckEntity);
            productEntity.setName(productName);
        }

        if (CollectionUtils.isNotEmpty(productEntity.getCover())) {
            productEntity.setCoverUrl(productEntity.getCover().get(0));
        }
    }

    private void saveProductGroupAttribute(List<ProductGroupEntity> addProductGroupList, List<ProductEntity> productEntityList) {
        if (CollectionUtils.isEmpty(addProductGroupList)) {
            return;
        }
        List<ProductEntity> addProductGroupEntityList = Lists.newArrayList();
        for (ProductEntity productEntity : productEntityList) {
            Optional<ProductGroupEntity> optional = addProductGroupList.stream()
                    .filter(x -> getProductGroupKey(x).equals(getProductGroupKey(productEntity.getProductGroupEntity()))).findFirst();
            if (optional.isPresent()) {
                productEntity.getProductGroupEntity().setId(optional.get().getId());
                addProductGroupEntityList.add(productEntity);
            }
        }

        List<ProductGroupAttributeEntity> productGroupAttributeEntities = convertProductGroupAttributeEntityList(addProductGroupEntityList);
        if (CollectionUtils.isNotEmpty(productGroupAttributeEntities)) {
            productGroupAttributeMapper.batchInsert(productGroupAttributeEntities);
        }
    }

    private void saveProductAttribute(List<ProductEntity> productEntityList) {
        List<ProductAttributeEntity> productAttributeEntities = convertProductAttributeEntityList(productEntityList);
        if (CollectionUtils.isNotEmpty(productAttributeEntities)) {
            productAttributeMapper.batchInsert(productAttributeEntities);
        }
    }

    private void savePhoto(List<ProductEntity> addList) {
        List<ProductPhotoEntity> productPhotoEntities = convertProductPhotoEntityList(addList);
        if (CollectionUtils.isNotEmpty(productPhotoEntities)) {
            productPhotoMapper.batchInsert(productPhotoEntities);
        }

        List<ProductPhotoEntity> addPhotoList = Lists.newArrayList();
        for (ProductEntity productEntity : addList) {
            if (CollectionUtils.isNotEmpty(productEntity.getCover())) {
                addPhotoList.add(createProductPhotoEntity(productEntity.getId(), productEntity.getCover().get(0), 0, PhotoTypeEnum.COVER));
            }

            if (CollectionUtils.isNotEmpty(productEntity.getSwiper())) {
                for (int i = 0; i < productEntity.getSwiper().size(); i++) {
                    addPhotoList.add(createProductPhotoEntity(productEntity.getId(), productEntity.getSwiper().get(i), i, PhotoTypeEnum.SWIPER));
                }
            }
        }

        productPhotoMapper.batchInsert(addPhotoList);
    }

    private ProductPhotoEntity createProductPhotoEntity(Long productId, String url, int index, PhotoTypeEnum photoTypeEnum) {
        ProductPhotoEntity productPhotoEntity = new ProductPhotoEntity();
        productPhotoEntity.setId(idGenerateHelper.nextId());
        FillUserUtil.fillCreateUserInfo(productPhotoEntity);
        productPhotoEntity.setProductId(productId);
        productPhotoEntity.setType(photoTypeEnum.getValue());
        productPhotoEntity.setName(photoTypeEnum.getDesc());
        productPhotoEntity.setUrl(url);
        productPhotoEntity.setSort(index + 1);
        return productPhotoEntity;
    }

    private void saveProductDetail(List<ProductEntity> addList) {
        List<ProductEntity> detailList = addList.stream().filter(x -> StringUtils.hasLength(x.getDetail())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(detailList)) {
            return;
        }

        List<ProductDetailEntity> addDetailList = detailList.stream().map(x -> {
            ProductDetailEntity productDetailEntity = new ProductDetailEntity();
            productDetailEntity.setProductId(x.getId());
            productDetailEntity.setDetail(x.getDetail());
            productDetailEntity.setId(idGenerateHelper.nextId());
            FillUserUtil.fillCreateUserInfo(productDetailEntity);
            return productDetailEntity;
        }).collect(Collectors.toList());
        mongoTemplate.insert(addDetailList, ProductDetailEntity.class);
    }

    private String getProductName(ProductEntity productEntity, ProductCheckEntity productCheckEntity) {
        CategoryEntity categoryEntity = productCheckEntity.getCategoryEntities().stream().filter(x -> x.getId().equals(productEntity.getCategoryId()))
                .findAny()
                .orElseThrow(() -> new BusinessException(String.format("分类ID%s在系统中不存在", productEntity.getCategoryId())));
        BrandEntity brandEntity = productCheckEntity.getBrandEntities().stream().filter(x -> x.getId().equals(productEntity.getBrandId()))
                .findAny()
                .orElseThrow(() -> new BusinessException(String.format("品牌ID%s在系统中不存在", productEntity.getBrandId())));
        return String.format("%s %s", categoryEntity.getName(), brandEntity.getName());
    }

    private void fillProductGroupExistId(List<ProductGroupEntity> productGroupEntityList, List<ProductGroupEntity> oldProductGroupEntities) {
        for (ProductGroupEntity productGroupEntity : productGroupEntityList) {
            Optional<ProductGroupEntity> productGroupEntityOptional = oldProductGroupEntities.stream()
                    .filter(x -> getProductGroupKey(x).equals(getProductGroupKey(productGroupEntity))).findAny();
            if (productGroupEntityOptional.isPresent()) {
                productGroupEntity.setId(productGroupEntityOptional.get().getId());
            }
        }
    }

    private void fillProductExistId(List<ProductEntity> productEntityList, List<ProductEntity> oldProductEntities) {
        for (ProductEntity productEntity : productEntityList) {
            Optional<ProductEntity> productEntityOptional = oldProductEntities.stream()
                    .filter(x -> getProductKey(x).equals(getProductKey(productEntity))).findAny();
            if (productEntityOptional.isPresent()) {
                productEntity.setId(productEntityOptional.get().getId());
            }
        }
    }

    private List<ProductGroupAttributeEntity> convertProductGroupAttributeEntityList(List<ProductEntity> productEntityList) {
        List<ProductGroupAttributeEntity> addProductAttributeList = Lists.newArrayList();
        for (ProductEntity productEntity : productEntityList) {
            List<ProductGroupAttributeEntity> productAttributeEntities = productEntity.getSpuAttributeEntityList().stream().map(x -> {
                ProductGroupAttributeEntity productAttributeEntity = new ProductGroupAttributeEntity();
                productAttributeEntity.setId(idGenerateHelper.nextId());
                productAttributeEntity.setProductGroupId(productEntity.getProductGroupEntity().getId());
                productAttributeEntity.setAttributeId(x.getAttributeId());
                productAttributeEntity.setAttributeValueId(x.getId());
                return productAttributeEntity;
            }).collect(Collectors.toList());

            addProductAttributeList.addAll(productAttributeEntities);
        }
        return addProductAttributeList;
    }

    private List<ProductAttributeEntity> convertProductAttributeEntityList(List<ProductEntity> productEntityList) {
        List<ProductAttributeEntity> addProductAttributeList = Lists.newArrayList();
        for (ProductEntity productEntity : productEntityList) {
            List<ProductAttributeEntity> productAttributeEntities = productEntity.getSkuAttributeEntityList().stream().map(x -> {
                ProductAttributeEntity productAttributeEntity = new ProductAttributeEntity();
                productAttributeEntity.setId(idGenerateHelper.nextId());
                productAttributeEntity.setProductId(productEntity.getId());
                productAttributeEntity.setAttributeId(x.getAttributeId());
                productAttributeEntity.setAttributeValueId(x.getId());
                return productAttributeEntity;
            }).collect(Collectors.toList());

            addProductAttributeList.addAll(productAttributeEntities);
        }
        return addProductAttributeList;
    }

    private List<ProductPhotoEntity> convertProductPhotoEntityList(List<ProductEntity> productEntityList) {
        List<ProductPhotoEntity> addProductPhotoList = Lists.newArrayList();
        for (ProductEntity productEntity : productEntityList) {
            if (CollectionUtils.isNotEmpty(productEntity.getProductPhotoEntityList())) {
                List<ProductPhotoEntity> productPhotoEntities = productEntity.getProductPhotoEntityList().stream().map(x -> {
                    ProductPhotoEntity productPhotoEntity = new ProductPhotoEntity();
                    productPhotoEntity.setId(idGenerateHelper.nextId());
                    productPhotoEntity.setProductId(productEntity.getId());
                    productPhotoEntity.setName(x.getName());
                    productPhotoEntity.setUrl(x.getUrl());
                    productPhotoEntity.setSort(x.getSort());
                    return productPhotoEntity;
                }).collect(Collectors.toList());

                addProductPhotoList.addAll(productPhotoEntities);
            }

        }
        return addProductPhotoList;
    }

    private String getProductKey(ProductEntity productEntity) {
        return String.format("%s_%s_%s_%s",
                productEntity.getCategoryId(),
                productEntity.getUnitId(),
                productEntity.getBrandId(),
                productEntity.getHash());
    }

    private void checkCategory(List<ProductEntity> productEntityList, ProductCheckEntity productCheckEntity) {
        CategoryConditionEntity categoryConditionEntity = new CategoryConditionEntity();
        List<Long> categoryIdList = productEntityList.stream().map(ProductEntity::getCategoryId).distinct().collect(Collectors.toList());
        categoryConditionEntity.setIdList(categoryIdList);
        categoryConditionEntity.setPageNo(0);
        List<CategoryEntity> categoryEntities = categoryMapper.searchByCondition(categoryConditionEntity);
        AssertUtil.notEmpty(categoryEntities, "分类不能为空");

        List<Long> notFoundList = categoryIdList.stream().filter(x -> categoryEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
        AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), String.format("分类ID：%s，在系统中不存在", notFoundList));

        productCheckEntity.setCategoryEntities(categoryEntities);
    }

    private void checkUnit(List<ProductEntity> productEntityList) {
        UnitConditionEntity unitConditionEntity = new UnitConditionEntity();
        List<Long> unitIdList = productEntityList.stream().map(ProductEntity::getUnitId).distinct().collect(Collectors.toList());
        unitConditionEntity.setIdList(unitIdList);
        unitConditionEntity.setPageSize(0);
        List<UnitEntity> unitEntities = unitMapper.searchByCondition(unitConditionEntity);
        AssertUtil.notEmpty(unitEntities, "单位不能为空");

        List<Long> notFoundList = unitIdList.stream().filter(x -> unitEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
        AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), String.format("单位ID：%s，在系统中不存在", notFoundList));
    }

    private void checkBrand(List<ProductEntity> productEntityList, ProductCheckEntity productCheckEntity) {
        BrandConditionEntity brandConditionEntity = new BrandConditionEntity();
        List<Long> brandIdList = productEntityList.stream().map(ProductEntity::getBrandId).distinct().collect(Collectors.toList());
        brandConditionEntity.setIdList(brandIdList);
        brandConditionEntity.setPageSize(0);
        List<BrandEntity> brandEntities = brandMapper.searchByCondition(brandConditionEntity);
        AssertUtil.notEmpty(brandEntities, "品牌不能为空");

        List<Long> notFoundList = brandIdList.stream().filter(x -> brandEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
        AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), String.format("品牌ID：%s，在系统中不存在", notFoundList));

        productCheckEntity.setBrandEntities(brandEntities);
    }

    private void checkAttribute(List<ProductEntity> productEntityList) {
        List<AttributeValueEntity> spuAttributeValueEntities = Lists.newArrayList();
        for (ProductEntity productEntity : productEntityList) {
            if (CollectionUtils.isNotEmpty(productEntity.getSpuAttributeEntityList())) {
                spuAttributeValueEntities.addAll(productEntity.getSpuAttributeEntityList());
            }
        }
        if (CollectionUtils.isNotEmpty(spuAttributeValueEntities)) {
            checkAttributeValue(spuAttributeValueEntities);
        }

        List<AttributeValueEntity> skuAttributeValueEntities = productEntityList.stream()
                .flatMap(x -> x.getSkuAttributeEntityList().stream()).collect(Collectors.toList());
        AssertUtil.notEmpty(skuAttributeValueEntities, "商品属性不能为空");
        checkAttributeValue(skuAttributeValueEntities);
    }

    private void checkAttributeValue(List<AttributeValueEntity> attributeValueEntities) {
        AttributeValueConditionEntity attributeValueConditionEntity = new AttributeValueConditionEntity();
        List<Long> attributeValueIdList = attributeValueEntities.stream().map(AttributeValueEntity::getId).distinct().collect(Collectors.toList());
        attributeValueConditionEntity.setIdList(attributeValueIdList);
        attributeValueConditionEntity.setPageSize(0);
        List<AttributeValueEntity> attributeValueEntityList = attributeValueMapper.searchByCondition(attributeValueConditionEntity);
        AssertUtil.notEmpty(attributeValueEntityList, "属性值不能为空");

        List<Long> notFoundList = attributeValueIdList.stream().filter(x -> attributeValueEntityList.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
        AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundList), String.format("属性值ID：%s，在系统中不存在", notFoundList));

        List<Long> attributeIdList = attributeValueEntityList.stream().map(AttributeValueEntity::getAttributeId).collect(Collectors.toList());
        AttributeConditionEntity attributeConditionEntity = new AttributeConditionEntity();
        attributeConditionEntity.setIdList(attributeIdList);
        attributeConditionEntity.setPageSize(0);
        List<AttributeEntity> attributeEntities = attributeMapper.searchByCondition(attributeConditionEntity);
        AssertUtil.notEmpty(attributeEntities, "属性不能为空");

        List<Long> notFoundAttributeList = attributeIdList.stream().filter(x -> attributeEntities.stream().noneMatch(c -> x.equals(c.getId()))).collect(Collectors.toList());
        AssertUtil.isTrue(CollectionUtils.isEmpty(notFoundAttributeList), String.format("属性ID：%s，在系统中不存在", notFoundAttributeList));

        Map<Long, List<AttributeValueEntity>> attributeValueMap = attributeValueEntityList.stream().collect(Collectors.groupingBy(AttributeValueEntity::getId));
        Map<Long, List<AttributeEntity>> attributeMap = attributeEntities.stream().collect(Collectors.groupingBy(AttributeEntity::getId));

        for (AttributeValueEntity attributeValueEntity : attributeValueEntities) {
            List<AttributeEntity> subAttributeEntities = attributeMap.get(attributeValueEntity.getAttributeId());
            AssertUtil.notEmpty(subAttributeEntities, String.format("属性ID：%s，在系统中不存在", attributeValueEntity.getAttributeId()));
            AttributeEntity attributeEntity = subAttributeEntities.get(0);
            attributeValueEntity.setAttributeName(attributeEntity.getName());

            List<AttributeValueEntity> subAttributeValueEntities = attributeValueMap.get(attributeValueEntity.getId());
            AssertUtil.notEmpty(subAttributeValueEntities, String.format("属性值ID：%s，在系统中不存在", attributeValueEntity.getId()));
            attributeValueEntity.setValue(subAttributeValueEntities.get(0).getValue());
        }
    }


    /**
     * 修改商品
     *
     * @param productEntity 商品信息
     * @return 结果
     */
    public void update(ProductEntity productEntity) {
        final ProductCheckEntity productCheckEntity = new ProductCheckEntity();
        checkParams(Lists.newArrayList(productEntity), productCheckEntity);
        productEntity.setModel(null);
        fillSkuNameAndModel(productEntity, productCheckEntity);

        final List<ProductEntity> updateList = Lists.newArrayList(productEntity);
        transactionTemplate.execute((status -> {
            deleteProductDetail(productEntity);
            saveProductDetail(updateList);
            deleteProductAttribute(productEntity);
            saveProductAttribute(updateList);
            deleteProductPhoto(productEntity);
            savePhoto(updateList);
            return productMapper.update(productEntity);
        }));
    }

    /**
     * 新增商品
     *
     * @param productEntity 商品信息
     * @return 结果
     */
    public void insert(ProductEntity productEntity) {
        generate(Lists.newArrayList(productEntity));
    }

    private void checkParam(ProductEntity productEntity) {
        if (!StringUtils.hasLength(productEntity.getAttributeValueIds())) {
            throw new BusinessException("属性值编号不能为空");
        }

        List<Long> attributeValueIdList = Arrays.stream(productEntity.getAttributeValueIds()
                .split(",")).map(Long::parseLong).collect(Collectors.toList());
        AttributeValueConditionEntity attributeValueConditionEntity = new AttributeValueConditionEntity();
        attributeValueConditionEntity.setIdList(attributeValueIdList);
        List<AttributeValueEntity> attributeValueEntities = attributeValueMapper.searchByCondition(attributeValueConditionEntity);
        List<Long> notFoundList = attributeValueIdList.stream().filter(x -> attributeValueEntities.stream().noneMatch(a -> a.getId().equals(x))).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(notFoundList)) {
            throw new BusinessException(String.format("属性值编号：%s，在系统中不存在", notFoundList));
        }

        productEntity.setSkuAttributeEntityList(attributeValueEntities);
    }

    /**
     * 批量删除商品对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<ProductEntity> entities = productMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "商品已被删除");

        ProductEntity entity = new ProductEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return productMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return productMapper;
    }

}
