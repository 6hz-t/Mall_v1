package cn.net.susan.controller.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.marketing.CouponUserProvideConditionEntity;
import cn.net.susan.entity.marketing.CouponUserProvideEntity;
import cn.net.susan.service.marketing.CouponUserProvideService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/couponUserProvide")
public class CouponUserProvideController {

	@Autowired
	private CouponUserProvideService couponUserProvideService;

	/**
	 * 通过id查询优惠券发放信息
	 *
	 * @param id 系统ID
	 * @return 优惠券发放信息
	 */
	@GetMapping("/findById")
	public CouponUserProvideEntity findById(Long id) {
		return couponUserProvideService.findById(id);
	}

	/**
    * 根据条件查询优惠券发放列表
    *
    * @param couponUserProvideConditionEntity 条件
    * @return 优惠券发放列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<CouponUserProvideEntity> searchByPage(@RequestBody CouponUserProvideConditionEntity couponUserProvideConditionEntity) {
		return couponUserProvideService.searchByPage(couponUserProvideConditionEntity);
	}


	/**
     * 添加优惠券发放
     *
     * @param couponUserProvideEntity 优惠券发放实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody CouponUserProvideEntity couponUserProvideEntity) {
		return couponUserProvideService.insert(couponUserProvideEntity);
	}

	/**
     * 修改优惠券发放
     *
     * @param couponUserProvideEntity 优惠券发放实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody CouponUserProvideEntity couponUserProvideEntity) {
		return couponUserProvideService.update(couponUserProvideEntity);
	}

	/**
     * 批量删除优惠券发放
     *
     * @param ids 优惠券发放ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return couponUserProvideService.deleteByIds(ids);
	}
}
