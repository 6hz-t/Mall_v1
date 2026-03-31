package cn.net.susan.controller.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.marketing.CouponConditionEntity;
import cn.net.susan.entity.marketing.CouponEntity;
import cn.net.susan.service.marketing.CouponService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/coupon")
public class CouponController {

	@Autowired
	private CouponService couponService;

	/**
	 * 通过id查询优惠券信息
	 *
	 * @param id 系统ID
	 * @return 优惠券信息
	 */
	@GetMapping("/findById")
	public CouponEntity findById(Long id) {
		return couponService.findById(id);
	}

	/**
    * 根据条件查询优惠券列表
    *
    * @param couponConditionEntity 条件
    * @return 优惠券列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<CouponEntity> searchByPage(@RequestBody CouponConditionEntity couponConditionEntity) {
		return couponService.searchByPage(couponConditionEntity);
	}


	/**
     * 添加优惠券
     *
     * @param couponEntity 优惠券实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody CouponEntity couponEntity) {
		return couponService.insert(couponEntity);
	}

	/**
     * 修改优惠券
     *
     * @param couponEntity 优惠券实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody CouponEntity couponEntity) {
		return couponService.update(couponEntity);
	}

	/**
     * 批量删除优惠券
     *
     * @param ids 优惠券ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return couponService.deleteByIds(ids);
	}
}
