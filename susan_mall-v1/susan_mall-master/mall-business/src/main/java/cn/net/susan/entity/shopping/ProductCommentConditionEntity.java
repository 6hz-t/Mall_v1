package cn.net.susan.entity.shopping;

import cn.net.susan.entity.RequestConditionEntity;
import lombok.Data;

import java.util.List;


@Data
public class ProductCommentConditionEntity extends RequestConditionEntity {

    /**
     * ID集合
     */
    private List<Long> idList;

    /**
     * ID
     */
    private Long id;
    /**
     * 父评论ID
     */
    private Long parentId;
    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品ID集合
     */
    private List<Long> productIdList;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评分
     */
    private Integer rating;
    /**
     * 创建人ID
     */
    private Long createUserId;
    /**
     * 创建人名称
     */
    private String createUserName;
    /**
     * 修改人ID
     */
    private Long updateUserId;
    /**
     * 修改人名称
     */
    private String updateUserName;
    /**
     * 是否删除 1：已删除 0：未删除
     */
    private Integer isDel;

    /**
     * 评论类型
     */
    private Integer type;
}
