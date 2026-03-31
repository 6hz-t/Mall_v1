package cn.net.susan.entity.marketing;

import cn.net.susan.entity.RequestConditionEntity;
import lombok.Data;

import java.util.List;
import java.util.Date;


@Data
public class CouponUserReceiveConditionEntity extends RequestConditionEntity {

    /**
     * ID集合
     */
    private List<Long> idList;

    /**
     * ID
     */
    private Long id;
    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 优惠券ID集合
     */
    private List<Long> couponIdList;

    /**
     * 用户ID，0表示所有用户
     */
    private Long userId;
    /**
     * 使用状态 1:已使用 0:未使用
     */
    private Integer useStatus;
    /**
     * 使用时间
     */
    private Date useTime;
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
}
