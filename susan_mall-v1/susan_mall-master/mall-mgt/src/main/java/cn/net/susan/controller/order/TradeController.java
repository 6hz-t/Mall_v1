package cn.net.susan.controller.order;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.order.TradeConditionEntity;
import cn.net.susan.entity.order.TradeEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.order.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;


@Validated
@Api(tags = "订单操作", description = "订单接口")
@RestController
@RequestMapping("/v1/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * 通过id查询订单信息
     *
     * @param id 系统ID
     * @return 订单信息
     */
    @ApiOperation(notes = "通过id查询订单信息", value = "通过id查询订单信息")
    @GetMapping("/findById")
    public TradeEntity findById(Long id) {
        return tradeService.findById(id);
    }

    /**
     * 根据条件查询订单列表
     *
     * @param tradeConditionEntity 条件
     * @return 订单列表
     */
    @ApiOperation(notes = "根据条件查询订单列表", value = "根据条件查询订单列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<TradeEntity> searchByPage(@RequestBody TradeConditionEntity tradeConditionEntity) {
        return tradeService.searchByPage(tradeConditionEntity);
    }

    /**
     * 修改订单
     *
     * @param tradeEntity 订单实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改订单", value = "修改订单")
    @PostMapping("/update")
    public int update(@RequestBody TradeEntity tradeEntity) {
        return tradeService.update(tradeEntity);
    }

    /**
     * 批量删除订单
     *
     * @param ids 订单ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除订单", value = "批量删除订单")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return tradeService.deleteByIds(ids);
    }

    /**
     * 批量发货
     *
     * @param ids 订单ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量发货", value = "批量发货")
    @PostMapping("/shippedByIds")
    public int shippedByIds(@RequestBody @NotNull List<Long> ids) {
        return tradeService.shippedByIds(ids);
    }


    /**
     * 导出订单数据
     *
     * @return 影响行数
     */
    @ExcelExport(ExcelBizTypeEnum.ORDER_TRADE)
    @ApiOperation(notes = "导出订单数据", value = "导出订单数据")
    @PostMapping("/export")
    public void export(@RequestBody TradeConditionEntity tradeConditionEntity) {
    }
}
