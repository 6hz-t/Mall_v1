package cn.net.susan.controller.web;

import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.aftersale.web.RefundConditionWebEntity;
import cn.net.susan.entity.aftersale.web.RefundWebEntity;
import cn.net.susan.service.aftersale.RefundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Validated
@Api(tags = "退货web操作", description = "退货web操作")
@RestController
@RequestMapping("/v1/web/refund")
public class WebRefundController {

    @Autowired
    private RefundService refundService;

    /**
     * 退货退款
     *
     * @param refundWebEntity 参数
     */
    @ApiOperation(notes = "退货退款", value = "退货退款")
    @PostMapping("/doRefund")
    public void doRefund(@RequestBody @Valid RefundWebEntity refundWebEntity) {
        refundService.doRefund(refundWebEntity);
    }

    /**
     * 根据条件查询当前用户的退货列表
     *
     * @param refundConditionWebEntity 条件
     * @return 退货列表
     */
    @ApiOperation(notes = "根据条件查询当前用户的订单列表", value = "根据条件查询当前用户的订单列表")
    @PostMapping("/searchUserRefundByPage")
    public ResponsePageEntity<RefundWebEntity> searchUserRefundByPage(@RequestBody RefundConditionWebEntity refundConditionWebEntity) {
        return refundService.searchUserRefundByPage(refundConditionWebEntity);
    }
}
