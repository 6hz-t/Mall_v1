package cn.net.susan.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.order.TradeDeliveryAddressConditionEntity;
import cn.net.susan.entity.order.TradeDeliveryAddressEntity;
import cn.net.susan.service.order.TradeDeliveryAddressService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/tradeDeliveryAddress")
public class TradeDeliveryAddressController {

	@Autowired
	private TradeDeliveryAddressService tradeDeliveryAddressService;

	/**
	 * 通过id查询订单收货地址信息
	 *
	 * @param id 系统ID
	 * @return 订单收货地址信息
	 */
	@GetMapping("/findById")
	public TradeDeliveryAddressEntity findById(Long id) {
		return tradeDeliveryAddressService.findById(id);
	}

	/**
    * 根据条件查询订单收货地址列表
    *
    * @param tradeDeliveryAddressConditionEntity 条件
    * @return 订单收货地址列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<TradeDeliveryAddressEntity> searchByPage(@RequestBody TradeDeliveryAddressConditionEntity tradeDeliveryAddressConditionEntity) {
		return tradeDeliveryAddressService.searchByPage(tradeDeliveryAddressConditionEntity);
	}


	/**
     * 添加订单收货地址
     *
     * @param tradeDeliveryAddressEntity 订单收货地址实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody TradeDeliveryAddressEntity tradeDeliveryAddressEntity) {
		return tradeDeliveryAddressService.insert(tradeDeliveryAddressEntity);
	}

	/**
     * 修改订单收货地址
     *
     * @param tradeDeliveryAddressEntity 订单收货地址实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody TradeDeliveryAddressEntity tradeDeliveryAddressEntity) {
		return tradeDeliveryAddressService.update(tradeDeliveryAddressEntity);
	}

	/**
     * 批量删除订单收货地址
     *
     * @param ids 订单收货地址ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return tradeDeliveryAddressService.deleteByIds(ids);
	}
}
