package cn.net.susan.entity.common;

import cn.net.susan.annotation.Sensitive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.net.susan.entity.BaseEntity;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonSmsRecordEntity extends BaseEntity {

	/**
	 * 手机号
	 */
	@Sensitive
	private String phone;

	/**
	 * 验证码
	 */
	private String smsCode;

	/**
	 * 有效期
	 */
	private Integer expireSecond;

	/**
	 * 发送时间
	 */
	private Date sendTime;
}
