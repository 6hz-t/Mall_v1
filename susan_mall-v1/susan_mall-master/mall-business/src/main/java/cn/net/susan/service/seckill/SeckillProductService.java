package cn.net.susan.service.seckill;

import cn.hutool.core.bean.BeanUtil;
import cn.net.susan.config.BusinessConfig;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductDetailConditionEntity;
import cn.net.susan.entity.mongo.ProductDetailEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.ProductPhotoConditionEntity;
import cn.net.susan.entity.mall.ProductPhotoEntity;
import cn.net.susan.entity.seckill.ESSeckillProductEntity;
import cn.net.susan.entity.seckill.SeckillProductConditionEntity;
import cn.net.susan.entity.seckill.SeckillProductDetailEntity;
import cn.net.susan.entity.seckill.SeckillProductEntity;
import cn.net.susan.enums.PhotoTypeEnum;
import cn.net.susan.es.EsTemplate;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.mall.ProductDetailMapper;
import cn.net.susan.mapper.mall.ProductMapper;
import cn.net.susan.mapper.mall.ProductPhotoMapper;
import cn.net.susan.mapper.seckill.SeckillProductMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.service.mall.ProductService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.net.susan.constant.KeyConstant.SECKILL_PRODUCT_DETAIL_PFREFIX;
import static cn.net.susan.constant.KeyConstant.SECKILL_PRODUCT_STOCK_PREFIX;


@Service
public class SeckillProductService extends BaseService<SeckillProductEntity, SeckillProductConditionEntity> {


    @Autowired
    private SeckillProductMapper seckillProductMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private EsTemplate esTemplate;
    @Autowired
    private BusinessConfig businessConfig;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private ProductPhotoMapper productPhotoMapper;
    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询秒杀商品信息
     *
     * @param id 秒杀商品ID
     * @return 秒杀商品信息
     */
    public SeckillProductEntity findById(Long id) {
        return seckillProductMapper.findById(id);
    }

    /**
     * 根据条件分页查询秒杀商品列表
     *
     * @param ESSeckillProductConditionEntity 秒杀商品信息
     * @return 秒杀商品集合
     */
    public ResponsePageEntity<SeckillProductEntity> searchByPage(SeckillProductConditionEntity ESSeckillProductConditionEntity) {
        ResponsePageEntity<SeckillProductEntity> responsePageEntity = super.searchByPage(ESSeckillProductConditionEntity);
        fillProduct(ESSeckillProductConditionEntity, responsePageEntity.getData());
        return responsePageEntity;
    }

    private void fillProduct(SeckillProductConditionEntity ESSeckillProductConditionEntity, List<SeckillProductEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<Long> productIdList = list.stream().map(SeckillProductEntity::getProductId).distinct().collect(Collectors.toList());
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setIdList(productIdList);
        productConditionEntity.setPageSize(ESSeckillProductConditionEntity.getPageSize());
        ResponsePageEntity<ProductEntity> responsePageEntity = productService.searchByPage(productConditionEntity);
        if (Objects.nonNull(responsePageEntity) && CollectionUtils.isNotEmpty(responsePageEntity.getData())) {
            for (SeckillProductEntity seckillProductEntity : list) {
                Optional<ProductEntity> optional = responsePageEntity.getData()
                        .stream().filter(x -> x.getId().equals(seckillProductEntity.getProductId())).findAny();
                if (optional.isPresent()) {
                    ProductEntity productEntity = optional.get();
                    seckillProductEntity.setName(productEntity.getName());
                    seckillProductEntity.setModel(productEntity.getModel());
                    seckillProductEntity.setCategoryName(productEntity.getCategoryName());
                    seckillProductEntity.setBrandName(productEntity.getBrandName());
                    seckillProductEntity.setUnitName(productEntity.getUnitName());
                    seckillProductEntity.setCostPrice(productEntity.getPrice());
                }
            }
        }

    }

    /**
     * 新增秒杀商品
     *
     * @param seckillProductEntity 秒杀商品信息
     * @return 结果
     */
    public void insert(SeckillProductEntity seckillProductEntity) {
        checkParam(seckillProductEntity);
        seckillProductMapper.insert(seckillProductEntity);
        syncToESAndRedis(seckillProductEntity);
    }


    private void syncToESAndRedis(SeckillProductEntity seckillProductEntity) {
        ProductPhotoConditionEntity productPhotoConditionEntity = new ProductPhotoConditionEntity();
        productPhotoConditionEntity.setProductId(seckillProductEntity.getProductId());
        List<ProductPhotoEntity> productPhotoEntities = productPhotoMapper.searchByCondition(productPhotoConditionEntity);
        ESSeckillProductEntity esSeckillProductEntity = convertESSeckillProductEntity(seckillProductEntity);

        if (CollectionUtils.isNotEmpty(productPhotoEntities)) {
            Optional<ProductPhotoEntity> photoEntityOptional = productPhotoEntities.stream()
                    .filter(x -> (PhotoTypeEnum.COVER.getValue().equals(x.getType()))).findAny();
            if (photoEntityOptional.isPresent()) {
                esSeckillProductEntity.setCover(photoEntityOptional.get().getUrl());
            }
        }
        esTemplate.insertOrUpdate(businessConfig.getSeckillProductEsIndexName(), esSeckillProductEntity);
        syscProductStockToRedis(esSeckillProductEntity);
        syncProductDetailToRedis(productPhotoEntities, esSeckillProductEntity);
    }

    private void syncProductDetailToRedis(List<ProductPhotoEntity> productPhotoEntities, ESSeckillProductEntity esSeckillProductEntity) {
        SeckillProductDetailEntity seckillProductDetailEntity = new SeckillProductDetailEntity();
        BeanUtil.copyProperties(esSeckillProductEntity, seckillProductDetailEntity, false);

        ProductDetailConditionEntity productDetailConditionEntity = new ProductDetailConditionEntity();
        productDetailConditionEntity.setProductId(esSeckillProductEntity.getProductId());
        List<ProductDetailEntity> productDetailEntities = productDetailMapper.searchByCondition(productDetailConditionEntity);
        if (CollectionUtils.isNotEmpty(productDetailEntities)) {
            seckillProductDetailEntity.setDetail(productDetailEntities.get(0).getDetail());
        }

        if (CollectionUtils.isNotEmpty(productPhotoEntities)) {
            List<String> swiper = productPhotoEntities.stream()
                    .filter(x -> (PhotoTypeEnum.SWIPER.getValue().equals(x.getType())))
                    .map(ProductPhotoEntity::getUrl).collect(Collectors.toList());
            seckillProductDetailEntity.setSwiper(swiper);
        }

        redisUtil.set(getSeckillProductDetailKey(esSeckillProductEntity.getId()), JSON.toJSONString(seckillProductDetailEntity));
    }

    private void syscProductStockToRedis(ESSeckillProductEntity esSeckillProductEntity) {
        redisUtil.increment(getSeckillProductStockKey(esSeckillProductEntity.getId()), esSeckillProductEntity.getWithHoldQuantity());
    }


    private String getSeckillProductDetailKey(String id) {
        return String.format("%s%s", SECKILL_PRODUCT_DETAIL_PFREFIX, id);
    }

    private String getSeckillProductStockKey(String id) {
        return String.format("%s%s", SECKILL_PRODUCT_STOCK_PREFIX, id);
    }

    private ESSeckillProductEntity convertESSeckillProductEntity(SeckillProductEntity seckillProductEntity) {
        ESSeckillProductEntity esSeckillProductEntity = new ESSeckillProductEntity();
        esSeckillProductEntity.setId(seckillProductEntity.getId().toString());
        esSeckillProductEntity.setProductId(seckillProductEntity.getProductId());
        esSeckillProductEntity.setName(seckillProductEntity.getName());
        esSeckillProductEntity.setModel(seckillProductEntity.getModel());
        esSeckillProductEntity.setCostPrice(seckillProductEntity.getCostPrice());
        esSeckillProductEntity.setPrice(seckillProductEntity.getPrice());
        esSeckillProductEntity.setStartTime(seckillProductEntity.getStartTime());
        esSeckillProductEntity.setEndTime(seckillProductEntity.getEndTime());
        esSeckillProductEntity.setRemainQuantity(seckillProductEntity.getRemainQuantity());
        esSeckillProductEntity.setWithHoldQuantity(seckillProductEntity.getWithHoldQuantity());
        return esSeckillProductEntity;
    }

    private void checkParam(SeckillProductEntity seckillProductEntity) {
        ProductEntity productEntity = productMapper.findById(seckillProductEntity.getProductId());
        AssertUtil.notNull(productEntity, "商品ID在系统中不存在");
    }

    /**
     * 修改秒杀商品
     *
     * @param seckillProductEntity 秒杀商品信息
     * @return 结果
     */
    public void update(SeckillProductEntity seckillProductEntity) {
        checkParam(seckillProductEntity);
        FillUserUtil.fillUpdateUserInfo(seckillProductEntity);
        seckillProductMapper.update(seckillProductEntity);
        syncToESAndRedis(seckillProductEntity);
    }

    /**
     * 批量删除秒杀商品对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<SeckillProductEntity> entities = seckillProductMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "秒杀商品已被删除");

        SeckillProductEntity entity = new SeckillProductEntity();
        FillUserUtil.fillUpdateUserInfo(entity);

        return transactionTemplate.execute((status -> {
            int count = seckillProductMapper.deleteByIds(ids, entity);
            try {
                //todo这里后面需要优化，逻辑可以迁移到MQ中
                esTemplate.deleteBatch(businessConfig.getSeckillProductEsIndexName(), ids);
                for (Long id : ids) {
                    redisUtil.del(getSeckillProductDetailKey(id.toString()));
                }
            } catch (Exception e) {
                throw new BusinessException("删除秒杀商品失败");
            }
            return count;
        }));
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return seckillProductMapper;
    }

}
