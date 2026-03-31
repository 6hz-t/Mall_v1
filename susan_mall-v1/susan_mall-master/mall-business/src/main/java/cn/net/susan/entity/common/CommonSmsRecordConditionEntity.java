package cn.net.susan.entity.common;

import cn.net.susan.entity.RequestConditionEntity;
import lombok.Data;
import java.util.List;
import java.util.Date;


@Data
public class CommonSmsRecordConditionEntity extends RequestConditionEntity {

   /**
    * ID集合
    */
    private List<Long> idList;

	/**
	 *  ID
     */
	private Long id;
	/**
	 *  手机号
     */
	private String phone;
	/**
	 *  验证码
     */
	private String smsCode;
	/**
	 *  有效期
     */
	private Integer expireSecond;
	/**
	 *  发送时间
     */
	private Date sendTime;
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
