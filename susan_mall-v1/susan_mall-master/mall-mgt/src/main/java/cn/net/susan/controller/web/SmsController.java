package cn.net.susan.controller.web;

import cn.net.susan.annotation.NoLogin;
import cn.net.susan.entity.common.web.CommonSmsRecordWebEntity;
import cn.net.susan.service.sms.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(tags = "短信操作", description = "短信操作")
@RestController
@RequestMapping("/v1/web/sms")
@Validated
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 发送短信
     *
     * @param commonSmsRecordWebEntity 请求实体
     * @return 短信实体
     */
    @NoLogin
    @ApiOperation(notes = "发送短信", value = "发送短信")
    @PostMapping("/sendSmsCode")
    public CommonSmsRecordWebEntity sendSmsCode(@RequestBody @Valid CommonSmsRecordWebEntity commonSmsRecordWebEntity) {
        return smsService.sendSmsCode(commonSmsRecordWebEntity);
    }
}
