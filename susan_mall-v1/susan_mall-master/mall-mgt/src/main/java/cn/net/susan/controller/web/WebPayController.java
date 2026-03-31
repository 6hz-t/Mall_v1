package cn.net.susan.controller.web;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.net.susan.annotation.NoLogin;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.entity.pay.web.PayWebEntity;
import cn.net.susan.integration.pay.AliPayIntegration;
import cn.net.susan.service.pay.PayService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Api(tags = "web支付操作", description = "web支付操作")
@RestController
@RequestMapping("/v1/web/pay")
@Validated
public class WebPayController {

    @Autowired
    private AliPayIntegration aliPayIntegration;
    @Autowired
    private PayService payService;

    /**
     * 模拟支付接口
     *
     * @param payWebEntity 参数
     * @return 是否支付成功
     */
    @PostMapping("/mockPay")
    public Boolean mockPay(@RequestBody @Valid PayWebEntity payWebEntity) {
        return payService.mockPay(payWebEntity);
    }

    /**
     * 支付接口
     *
     * @param tradeEntity 订单实体
     * @param response    响应
     * @throws Exception
     */
    @NoLogin
    @PostMapping("/doPay")
    public void doPay(@RequestBody TradeEntity tradeEntity, HttpServletResponse response) throws Exception {
        String qrUrl = aliPayIntegration.pay(tradeEntity);
        QrCodeUtil.generate(qrUrl, 300, 300, "png", response.getOutputStream());
    }
}
