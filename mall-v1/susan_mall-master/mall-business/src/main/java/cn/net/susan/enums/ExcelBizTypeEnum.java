package cn.net.susan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ExcelBizTypeEnum {

    MENU(1, "cn.net.susan.entity.sys.MenuConditionEntity", "菜单"),
    ROLE(2, "cn.net.susan.entity.sys.RoleConditionEntity", "角色"),
    DEPT(3, "cn.net.susan.entity.sys.DeptConditionEntity", "部门"),
    USER(4, "cn.net.susan.entity.sys.UserConditionEntity", "用户"),
    JOB(5, "cn.net.susan.entity.sys.JobConditionEntity", "岗位"),
    UNIT(101, "cn.net.susan.entity.mall.UnitConditionEntity", "单位"),
    BRAND(102, "cn.net.susan.entity.mall.BrandConditionEntity", "品牌"),
    ATTRIBUTE(103, "cn.net.susan.entity.mall.AttributeConditionEntity", "属性"),
    ATTRIBUTE_VALUE(104, "cn.net.susan.entity.mall.AttributeValueConditionEntity", "属性值"),
    CATEGORY(105, "cn.net.susan.entity.mall.CategoryConditionEntity", "分类"),
    PRODUCT(106, "cn.net.susan.entity.mall.ProductConditionEntity", "商品"),
    COMMON_PHOTO_GROUP(110, "cn.net.susan.entity.common.CommonPhotoGroupConditionEntity", "图片组"),
    COMMON_NOTIFY(111, "cn.net.susan.entity.common.CommonNotifyConditionEntity", "通知"),
    COMMON_JOB(112, "cn.net.susan.entity.common.CommonJobConditionEntity", "定时任务"),
    ORDER_TRADE(120, "cn.net.susan.entity.order.TradeConditionEntity", "订单");

    /**
     * 枚举值
     */
    private Integer value;

    /**
     * 请求参数实体
     */
    private String requestEntity;

    /**
     * 枚举描述
     */
    private String desc;
}
