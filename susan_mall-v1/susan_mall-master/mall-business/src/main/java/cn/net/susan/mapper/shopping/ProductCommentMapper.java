package cn.net.susan.mapper.shopping;

import cn.net.susan.entity.shopping.ProductCommentConditionEntity;
import cn.net.susan.entity.shopping.ProductCommentEntity;

import java.util.List;

import cn.net.susan.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface ProductCommentMapper extends BaseMapper<ProductCommentEntity, ProductCommentConditionEntity> {
    /**
     * 查询商品评论信息
     *
     * @param id 商品评论ID
     * @return 商品评论信息
     */
    ProductCommentEntity findById(Long id);

    /**
     * 添加商品评论
     *
     * @param productCommentEntity 商品评论信息
     * @return 结果
     */
    int insert(ProductCommentEntity productCommentEntity);

    /**
     * 批量评价
     *
     * @param list 评价集合
     * @return 结果
     */
    int batchInsert(List<ProductCommentEntity> list);

    /**
     * 修改商品评论
     *
     * @param productCommentEntity 商品评论信息
     * @return 结果
     */
    int update(ProductCommentEntity productCommentEntity);

    /**
     * 批量删除商品评论
     *
     * @param ids    id集合
     * @param entity 商品评论实体
     * @return 结果
     */
    int deleteByIds(@Param("ids") List<Long> ids, @Param("entity") ProductCommentEntity entity);

    /**
     * 批量查询商品评论信息
     *
     * @param ids ID集合
     * @return 商品评论信息
     */
    List<ProductCommentEntity> findByIds(List<Long> ids);
}
