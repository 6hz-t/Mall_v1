package cn.net.susan.entity.mall.web;

import lombok.Data;

import java.util.Date;


@Data
public class ProductCommentWebEntity {

    /**
     * 系统ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 个人头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评分
     */
    private Integer rating;

    /**
     * 评价类型 1：好评 2：中评 3：差评
     */
    private Integer type;

    /**
     * 评价时间
     */
    private String createTimeStr;
}
