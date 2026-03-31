package cn.net.susan.controller.web;

import cn.net.susan.annotation.NoLogin;
import cn.net.susan.annotation.RepeatSubmit;
import cn.net.susan.annotation.VerifySign;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.entity.order.web.TradeDetailWebEntity;
import cn.net.susan.entity.order.web.TradeItemReqWebEntity;
import cn.net.susan.entity.order.web.TradeItemWebEntity;
import cn.net.susan.entity.order.web.TradeOperateWebEntity;
import cn.net.susan.entity.order.web.TradeConditionWebEntity;
import cn.net.susan.entity.order.web.TradeConfirmReqWebEntity;
import cn.net.susan.entity.order.web.TradeConfirmWebEntity;
import cn.net.susan.entity.order.web.TradeSubmitWebEntity;
import cn.net.susan.entity.order.web.TradeWebEntity;
import cn.net.susan.entity.order.web.UserTradeCountWebEntity;
import cn.net.susan.entity.seckill.UserSeckillProductTradeEntity;
import cn.net.susan.service.order.TradeService;
import cn.net.susan.util.OrderCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Validated
@Api(tags = "订单操作", description = "订单接口")
@RestController
@RequestMapping("/v1/web/trade")
public class WebTradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * 获取订单明细
     *
     * @param tradeItemReqWebEntity 查询条件
     * @return 订单明细
     */
    @ApiOperation(notes = "获取订单明细", value = "获取订单明细")
    @PostMapping("/getTradeItem")
    public TradeItemWebEntity getTradeItem(@RequestBody @NotNull TradeItemReqWebEntity tradeItemReqWebEntity) {
        return tradeService.getTradeItem(tradeItemReqWebEntity);
    }

    /**
     * 获取用户订单数量统计
     *
     * @return 用户订单数量统计
     */
    @ApiOperation(notes = "获取用户订单数量统计", value = "获取用户订单数量统计")
    @GetMapping("/getUserOrderTradeCount")
    public UserTradeCountWebEntity getUserOrderTradeCount() {
        return tradeService.getUserOrderTradeCount();
    }

    /**
     * 根据code查询订单详情
     *
     * @param code 订单编号
     * @return 订单详情
     */
    @ApiOperation(notes = "根据code查询订单详情", value = "根据code查询订单详情")
    @GetMapping("/getDetail/{code}")
    public TradeDetailWebEntity getDetail(@PathVariable("code") String code) {
        return tradeService.getDetail(code);
    }

    /**
     * 根据条件查询当前用户的订单列表
     *
     * @param tradeConditionWebEntity 条件
     * @return 订单列表
     */
    @ApiOperation(notes = "根据条件查询当前用户的订单列表", value = "根据条件查询当前用户的订单列表")
    @PostMapping("/searchUserTradeByPage")
    public ResponsePageEntity<TradeWebEntity> searchUserTradeByPage(@RequestBody TradeConditionWebEntity tradeConditionWebEntity) {
        return tradeService.searchUserTradeByPage(tradeConditionWebEntity);
    }

    /**
     * 确认订单
     *
     * @return 订单实体
     */
    @ApiOperation(notes = "确认订单", value = "确认订单")
    @PostMapping("/confirm")
    public TradeConfirmWebEntity confirm(@RequestBody @Valid TradeConfirmReqWebEntity tradeConfirmReqWebEntity) {
        return tradeService.confirm(tradeConfirmReqWebEntity);
    }

    /**
     * 提交订单
     *
     * @return 订单实体
     */
    @ApiOperation(notes = "提交订单", value = "提交订单")
    @PostMapping("/submit")
    public TradeConfirmWebEntity submit(@RequestBody @Valid TradeSubmitWebEntity tradeSubmitWebEntity) {
        return tradeService.submit(tradeSubmitWebEntity);
    }

    /**
     * 取消订单
     *
     * @return 订单实体
     */
    @ApiOperation(notes = "取消订单", value = "取消订单")
    @PostMapping("/cancel")
    public void cancel(@RequestBody @Valid TradeOperateWebEntity tradeOperateWebEntity) {
        tradeService.cancel(tradeOperateWebEntity);
    }

    /**
     * 删除订单
     *
     * @return 订单实体
     */
    @ApiOperation(notes = "删除订单", value = "删除订单")
    @PostMapping("/delete")
    public void delete(@RequestBody @Valid TradeOperateWebEntity tradeOperateWebEntity) {
        tradeService.delete(tradeOperateWebEntity);
    }

    /**
     * 确认收货
     *
     * @return 订单实体
     */
    @ApiOperation(notes = "确认收货", value = "确认收货")
    @PostMapping("/confirmReceive")
    public void confirmReceive(@RequestBody @Valid TradeOperateWebEntity tradeOperateWebEntity) {
        tradeService.confirmReceive(tradeOperateWebEntity);
    }

    /**
     * 生成订单code
     *
     * @return 订单code
     */
    @ApiOperation(notes = "生成订单code", value = "生成订单code")
    @GetMapping("/generateCode")
    public TradeEntity generateCode() {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setCode(OrderCodeUtil.generateOrderCode());
        return tradeEntity;
    }


    /**
     * 用户下单
     *
     * @param tradeEntity 订单实体
     * @return 影响行数
     */
    @RepeatSubmit
    @ApiOperation(notes = "用户下单", value = "用户下单")
    @PostMapping("/create")
    public TradeEntity create(@RequestBody @Valid TradeEntity tradeEntity) {
        tradeService.createOrderTrade(tradeEntity);
        return tradeEntity;
    }

    /**
     * 创建秒杀订单
     *
     * @param userSeckillProductTradeEntity 订单实体
     * @return 秒杀订单
     */
    @VerifySign
    @NoLogin
    @ApiOperation(notes = "创建秒杀订单", value = "创建秒杀订单")
    @PostMapping("/createForSeckill")
    public UserSeckillProductTradeEntity createForSeckill(@RequestBody @Valid UserSeckillProductTradeEntity userSeckillProductTradeEntity) {
        return tradeService.createUserSeckillProductTrade(userSeckillProductTradeEntity);
    }
}
