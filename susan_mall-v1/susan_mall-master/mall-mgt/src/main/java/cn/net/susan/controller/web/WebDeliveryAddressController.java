package cn.net.susan.controller.web;

import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.entity.shopping.DeliveryAddressEntity;
import cn.net.susan.entity.shopping.web.DeliveryAddressDefaultWebEntity;
import cn.net.susan.entity.shopping.web.DeliveryAddressWebEntity;
import cn.net.susan.service.marketing.CouponService;
import cn.net.susan.service.shopping.DeliveryAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = "web端收货地址操作", description = "web端收货地址操作")
@RestController
@RequestMapping("/v1/web/deliveryAddress")
@Validated
public class WebDeliveryAddressController {

    @Autowired
    private DeliveryAddressService deliveryAddressService;


    /**
     * 设置默认收货地址
     *
     * @param deliveryAddressWebEntity 收货地址实体
     */
    @ApiOperation(notes = "设置默认收货地址", value = "设置默认收货地址")
    @PostMapping("/setDefaultDeliveryAddress")
    public void setDefaultDeliveryAddress(@RequestBody @Valid DeliveryAddressDefaultWebEntity deliveryAddressWebEntity) {
        deliveryAddressService.setDefaultDeliveryAddress(deliveryAddressWebEntity);
    }


    /**
     * 获取某用户收货地址列表
     *
     * @return 收货地址列表
     */
    @ApiOperation(notes = "获取某用户收货地址列表", value = "获取某用户收货地址列表")
    @GetMapping("/getUserDeliveryAddressList")
    public List<DeliveryAddressWebEntity> getUserDeliveryAddressList() {
        return deliveryAddressService.getUserDeliveryAddressList();
    }

    /**
     * 获取收货地址详情
     *
     * @return 收货地址详情
     */
    @ApiOperation(notes = "获取收货地址详情", value = "获取收货地址详情")
    @GetMapping("/getDetail")
    public DeliveryAddressWebEntity getDetail(@RequestParam("id") Long id) {
        return deliveryAddressService.getDetail(id);
    }

    /**
     * 批量删除收货地址
     *
     * @param ids 收货地址ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除收货地址", value = "批量删除收货地址")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return deliveryAddressService.deleteByIds(ids);
    }

    /**
     * 保存收货地址
     *
     * @param deliveryAddressWebEntity 收货地址实体
     * @return 影响行数
     */
    @ApiOperation(notes = "保存收货地址", value = "保存收货地址")
    @PostMapping("/save")
    public void save(@RequestBody @Valid DeliveryAddressWebEntity deliveryAddressWebEntity) {
        deliveryAddressService.save(deliveryAddressWebEntity);
    }
}
