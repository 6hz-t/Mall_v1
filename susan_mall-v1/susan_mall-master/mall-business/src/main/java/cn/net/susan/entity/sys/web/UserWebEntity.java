package cn.net.susan.entity.sys.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@ApiModel("用户web实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserWebEntity  {

    /**
     * 系统ID
     */
    private Long id;

    /**
     * 头像url
     */
    @ApiModelProperty("头像url")
    private String avatarUrl;

    /**
     * 邮箱
     */
    @NotEmpty(message = "邮箱不能为空")
    @ApiModelProperty("邮箱")
    private String email;


    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;


    /**
     * 别名
     */
    @ApiModelProperty("别名")
    private String nickName;

    /**
     * 性别 1：男 2：女
     */
    @ApiModelProperty("性别 1：男 2：女")
    private Integer sex;
}
