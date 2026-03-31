package cn.net.susan.entity.aftersale;

import cn.net.susan.entity.RequestConditionEntity;
import lombok.Data;
import java.util.List;


@Data
public class RefundPhotoConditionEntity extends RequestConditionEntity {

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
	 *  图片名称
     */
	private String name;
	/**
	 *  图片url
     */
	private String url;
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
