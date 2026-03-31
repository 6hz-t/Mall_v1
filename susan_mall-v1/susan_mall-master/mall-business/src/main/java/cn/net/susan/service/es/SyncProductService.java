package cn.net.susan.service.es;

import cn.net.susan.config.BusinessConfig;
import cn.net.susan.constant.NumberConstant;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.web.ProductWebEntity;
import cn.net.susan.entity.order.TradeItemEntity;
import cn.net.susan.entity.shopping.ProductCommentConditionEntity;
import cn.net.susan.enums.OrderStatusEnum;
import cn.net.susan.enums.ProductCommentTypeEnum;
import cn.net.susan.es.EsTemplate;
import cn.net.susan.mapper.shopping.ProductCommentMapper;
import cn.net.susan.service.mall.ProductService;
import cn.net.susan.service.order.TradeItemService;
import cn.net.susan.util.BigDecimalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SyncProductService {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    @Autowired
    private ProductService productService;
    @Autowired
    private EsTemplate esTemplate;
    @Autowired
    private BusinessConfig businessConfig;
    @Autowired
    private ProductCommentMapper productCommentMapper;
    @Autowired
    private TradeItemService tradeItemService;

    /**
     * 同步商品到ES
     */
    public void syncProductToES() {
        handleInsertOrUpdate();
        handleDelete();
    }

    private void handleInsertOrUpdate() {
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setPageSize(NumberConstant.NUMBER_500);
        productConditionEntity.setIsDel(0);
        ResponsePageEntity<ProductEntity> productEntityResponsePageEntity = productService.searchByPage(productConditionEntity);

        while (CollectionUtils.isNotEmpty(productEntityResponsePageEntity.getData())) {
            saveData(productEntityResponsePageEntity.getData());

            productConditionEntity.setPageNo(productConditionEntity.getPageNo() + 1);
            productEntityResponsePageEntity = productService.searchByPage(productConditionEntity);
        }
    }

    private void handleDelete() {
        ProductConditionEntity productConditionEntity = new ProductConditionEntity();
        productConditionEntity.setPageSize(NumberConstant.NUMBER_500);
        productConditionEntity.setIsDel(1);
        ResponsePageEntity<ProductEntity> productEntityResponsePageEntity = productService.searchByPage(productConditionEntity);

        while (CollectionUtils.isNotEmpty(productEntityResponsePageEntity.getData())) {
            List<Long> idList = productEntityResponsePageEntity.getData().stream().map(ProductEntity::getId).collect(Collectors.toList());
            try {
                esTemplate.deleteBatch(businessConfig.getProductEsIndexName(), idList);
            } catch (IOException e) {
                log.error("删除ES中的商品失败，原因：", e);
            }

            productConditionEntity.setPageNo(productConditionEntity.getPageNo() + 1);
            productEntityResponsePageEntity = productService.searchByPage(productConditionEntity);
        }
    }


    private void saveData(List<ProductEntity> productEntities) {
        List<ProductWebEntity> dataList = productEntities.stream().map(x -> {
            ProductWebEntity productWebEntity = new ProductWebEntity();
            productWebEntity.setCategoryId(x.getCategoryId());
            productWebEntity.setId(x.getId().toString());
            productWebEntity.setName(x.getName());
            productWebEntity.setModel(x.getModel());
            productWebEntity.setPrice(x.getPrice().toString());
            productWebEntity.setQuantity(x.getQuantity());
            productWebEntity.setCover(x.getCoverUrl());
            return productWebEntity;
        }).collect(Collectors.toList());

        for (ProductWebEntity productWebEntity : dataList) {
            statSaleCount(productWebEntity);
            statPositiveRating(productWebEntity);
            esTemplate.insertOrUpdate(businessConfig.getProductEsIndexName(), productWebEntity);
        }

        //esTemplate.batchInsert(businessConfig.getProductEsIndexName(), dataList);
    }


    private void statSaleCount(ProductWebEntity productWebEntity) {
        TradeItemEntity tradeItemEntity = tradeItemService.statProductSaleQuantity(Long.parseLong(productWebEntity.getId()),
                OrderStatusEnum.FINISH.getValue());
        if (Objects.nonNull(tradeItemEntity)) {
            productWebEntity.setSaleQuantity(tradeItemEntity.getQuantity().toString());
        } else {
            productWebEntity.setSaleQuantity("0");
        }
    }

    private void statPositiveRating(ProductWebEntity productWebEntity) {
        long productId = Long.parseLong(productWebEntity.getId());
        ProductCommentConditionEntity productCommentConditionEntity = new ProductCommentConditionEntity();
        productCommentConditionEntity.setProductId(productId);
        int totalCount = productCommentMapper.searchCount(productCommentConditionEntity);
        productWebEntity.setCommentCount("totalCount");

        productCommentConditionEntity.setType(ProductCommentTypeEnum.POSITIVE.getValue());
        int positiveCount = productCommentMapper.searchCount(productCommentConditionEntity);
        if (totalCount == 0) {
            productWebEntity.setPositiveRating("100");
        } else {
            if (positiveCount == 0) {
                productWebEntity.setPositiveRating("0");
            } else {
                log.info("positiveCount:{},totalCount:{}", positiveCount, totalCount);
                productWebEntity.setPositiveRating(String.valueOf(calcPositiveRating(positiveCount, totalCount)));
            }
        }
    }

    private int calcPositiveRating(int positiveCount, int totalCount) {
        return BigDecimalUtil.round(new BigDecimal(positiveCount)
                .multiply(ONE_HUNDRED)
                .divide(new BigDecimal(totalCount), BigDecimal.ROUND_HALF_UP), 0).intValue();
    }
}
