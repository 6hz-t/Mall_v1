package cn.net.susan.entity.log;

import cn.net.susan.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("业务日志实体")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BizLogEntity extends BaseEntity {


    /**
     * 方法名称
     */
    @ApiModelProperty("方法名称")
    private String methodName;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 请求ip
     */
    @ApiModelProperty("请求ip")
    private String requestIp;

    /**
     * 浏览器
     */
    @ApiModelProperty("浏览器")
    private String browser;

    /**
     * 请求地址
     */
    @ApiModelProperty("请求地址")
    private String url;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    private String param;

    /**
     * 耗时
     */
    @ApiModelProperty("耗时")
    private Integer time;

    /**
     * 异常
     */
    @ApiModelProperty("异常")
    private String exception;

    /**
     * 状态 1:成功 0:失败
     */
    @ApiModelProperty("状态 1:成功 0:失败")
    private Integer status;

    /**
     * 所在城市
     */
    @ApiModelProperty("所在城市")
    private String city;
}
