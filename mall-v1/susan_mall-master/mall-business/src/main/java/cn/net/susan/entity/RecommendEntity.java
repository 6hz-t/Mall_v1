package cn.net.susan.entity;

import lombok.Data;



@Data
public class RecommendEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 业务ID
     */
    private Long itemId;

    /**
     * 值
     */
    private float value;
}
