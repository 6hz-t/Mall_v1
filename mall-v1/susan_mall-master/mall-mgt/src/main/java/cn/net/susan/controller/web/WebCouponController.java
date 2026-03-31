package cn.net.susan.controller.web;

import cn.net.susan.entity.mall.web.ProductWebEntity;
import cn.net.susan.entity.marketing.web.CouponWebEntity;
import cn.net.susan.service.marketing.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "web优惠券操作", description = "web优惠券操作")
@RestController
@RequestMapping("/v1/web/coupon")
@Validated
public class WebCouponController {

    @Autowired
    private CouponService couponService;

    /**
     * 用户领取优惠券
     *
     * @param couponWebEntity 优惠券
     */
    @PostMapping("/receiveCoupon")
    public void receiveCoupon(@RequestBody @Valid CouponWebEntity couponWebEntity) {
        couponService.receiveCoupon(couponWebEntity);
    }


    /**
     * 获取可领取的优惠券列表
     *
     * @return 优惠券列表
     */
    @ApiOperation(notes = "获取可领取的优惠券列表", value = "获取可领取的优惠券列表")
    @GetMapping("/getObtainableCouponList")
    public List<CouponWebEntity> getObtainableCouponList() {
        return couponService.getObtainableCouponList();
    }

    /**
     * 获取某用户已经领取的优惠券列表
     *
     * @return 商品列表
     */
    @ApiOperation(notes = "获取某用户已经领取的优惠券列表", value = "获取某用户已经领取的优惠券列表")
    @GetMapping("/getUserCouponList")
    public List<CouponWebEntity> getUserCouponList() {
        return couponService.getUserCouponList();
    }
}
