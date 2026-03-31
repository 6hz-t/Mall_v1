package cn.net.susan.entity.common.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonSmsRecordWebEntity {

    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    /**
     * 验证码uuid
     */
    @NotEmpty(message = "验证码uuid不能为空")
    private String captchaUuid;

    /**
     * 验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String captchaCode;

    /**
     * 短信类型
     */
    @NotNull(message = "短信类型不能为空")
    private Integer type;

    /**
     * 有效期
     */
    private Long expireSecond;


}
