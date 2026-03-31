package cn.net.susan.entity.sys.web;

import cn.net.susan.annotation.ValidPhone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@ApiModel("用户手机号登录web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPhoneLoginWebEntity {

    /**
     * 手机号
     */
    @ValidPhone
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 短信验证码
     */
    @ApiModelProperty("短信验证码")
    private String smsCode;
}
