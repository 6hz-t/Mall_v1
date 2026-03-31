package cn.net.susan.entity.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCommentEntity extends BaseEntity {


    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 商品ID
     */
    private Long productId;

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
     * 评论类型
     */
    private Integer type;
}
