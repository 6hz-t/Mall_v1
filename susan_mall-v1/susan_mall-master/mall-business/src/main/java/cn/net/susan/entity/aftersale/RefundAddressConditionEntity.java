package cn.net.susan.entity.aftersale;

import cn.net.susan.entity.RequestConditionEntity;
import lombok.Data;
import java.util.List;


@Data
public class RefundAddressConditionEntity extends RequestConditionEntity {

   /**
    * ID集合
    */
    private List<Long> idList;

	/**
	 *  ID
     */
	private Long id;
	/**
	 *  退货单ID
     */
	private Long refundId;
	/**
	 *  收货人姓名
     */
	private String receiverName;
	/**
	 *  收货人手机号
     */
	private String receiverPhone;
	/**
	 *  省份
     */
	private String receiverProvince;
	/**
	 *  城市
     */
	private String city;
	/**
	 *  区县
     */
	private String district;
	/**
	 *  详细地址
     */
	private String detailAddress;
	/**
	 *  邮编
     */
	private String postCode;
	/**
	 *  创建人ID
     */
	private Long createUserId;
	/**
	 *  创建人名称
     */
	private String createUserName;
	/**
	 *  修改人ID
     */
	private Long updateUserId;
	/**
	 *  修改人名称
     */
	private String updateUserName;
	/**
	 *  是否删除 1：已删除 0：未删除
     */
	private Integer isDel;
}
