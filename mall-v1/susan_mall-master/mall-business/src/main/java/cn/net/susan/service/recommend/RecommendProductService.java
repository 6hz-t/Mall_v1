package cn.net.susan.service.recommend;

import cn.hutool.json.JSONUtil;
import cn.net.susan.config.BusinessConfig;
import cn.net.susan.entity.RecommendEntity;
import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.mall.web.ProductWebEntity;
import cn.net.susan.entity.shopping.ProductViewRecordConditionEntity;
import cn.net.susan.entity.shopping.ProductViewRecordEntity;
import cn.net.susan.es.EsTemplate;
import cn.net.susan.factory.RecommendFactory;
import cn.net.susan.mapper.shopping.ProductViewRecordMapper;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
public class RecommendProductService {
    private static final String USER_RECOMMEND_PRODUCT_PREFIX = "userRecommendProduct:";

    @Autowired
    private ProductViewRecordMapper productViewRecordMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BusinessConfig businessConfig;
    @Autowired
    private EsTemplate esTemplate;

    @Value("${mall.mgt.recommendProductCount:2}")
    private int recommendProductCount;

    /**
     * 推荐商品
     */
    public void recommendProductToRedis() {
        ProductViewRecordConditionEntity productViewRecordConditionEntity = new ProductViewRecordConditionEntity();
        productViewRecordConditionEntity.setPageSize(0);
        List<ProductViewRecordEntity> productViewRecordEntities = productViewRecordMapper.searchByCondition(productViewRecordConditionEntity);
        if (CollectionUtils.isEmpty(productViewRecordEntities)) {
            return;
        }

        try {
            List<Long> userIdList = productViewRecordEntities.stream().map(ProductViewRecordEntity::getUserId).distinct().collect(Collectors.toList());

            List<RecommendEntity> list = convertRecommendEntity(productViewRecordEntities);
            //创建模型数据
            DataModel dataModel = RecommendFactory.buildJdbcDataModel(list);
            //2.创建similar相似度
            UserSimilarity similarity = new UncenteredCosineSimilarity(dataModel);
            //3.获取用户userNeighborhood
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);

            //4.构建推荐器recommend
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

            for (Long userId : userIdList) {
                try {
                    List<Long> recommendProductIdList = getRecommendProductIdList(recommender, userId);
                    redisUtil.set(getKey(userId), JSONUtil.toJsonStr(recommendProductIdList));
                } catch (TasteException e) {
                    log.info(String.format("recommendProductToRedis给用户[%s]推荐商品失败，原因：%s", userId, e.getMessage()));
                }
            }
        } catch (TasteException e) {
            log.error("recommendProductToRedis方法执行失败，原因：", e);
        }
    }

    private String getKey(Long userId) {
        return String.format("%s%s", USER_RECOMMEND_PRODUCT_PREFIX, userId);
    }


    private List<Long> getRecommendProductIdList(Recommender recommender, Long userId) throws TasteException {
        //展示类似的5个商品
        List<RecommendedItem> recommendedItems = recommender.recommend(userId, recommendProductCount);
        return recommendedItems.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
    }

    /**
     * 获取用户喜好，推荐相似的商品
     *
     * @return 相似的商品列表
     * @throws TasteException 异常
     */
    public List<ProductWebEntity> recommendProduct() {
        JwtUserEntity userEntity = FillUserUtil.getCurrentUserInfoOrNull();
        if (Objects.nonNull(userEntity)) {
            String json = redisUtil.get(getKey(userEntity.getId()));
            if (StringUtils.hasLength(json)) {
                List<Long> productIdList = JSONUtil.toList(json, Long.class);

                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
                List<String> idsList = productIdList.stream().map(String::valueOf).collect(Collectors.toList());
                idsQueryBuilder.addIds(idsList.toArray(new String[0]));
                searchSourceBuilder.query(idsQueryBuilder);

                try {
                    log.info("recommendProduct请求参数:", searchSourceBuilder);
                    return esTemplate.search(businessConfig.getProductEsIndexName(), searchSourceBuilder, ProductWebEntity.class);
                } catch (IOException e) {
                    log.warn("recommendProduct 从ES中获取失败失败，原因：", e);
                }
            }
        }

        return Collections.emptyList();
    }


    private List<RecommendEntity> convertRecommendEntity(List<ProductViewRecordEntity> productViewRecordEntities) {
        return productViewRecordEntities.stream().map(x -> {
            RecommendEntity recommendEntity = new RecommendEntity();
            recommendEntity.setUserId(x.getUserId());
            recommendEntity.setItemId(x.getProductId());
            recommendEntity.setValue(x.getViewCount());
            return recommendEntity;
        }).collect(Collectors.toList());
    }
}
