package cn.net.susan.controller.shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.shopping.DeliveryAddressConditionEntity;
import cn.net.susan.entity.shopping.DeliveryAddressEntity;
import cn.net.susan.service.shopping.DeliveryAddressService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/deliveryAddress")
public class DeliveryAddressController {

	@Autowired
	private DeliveryAddressService deliveryAddressService;

	/**
	 * 通过id查询收货地址信息
	 *
	 * @param id 系统ID
	 * @return 收货地址信息
	 */
	@GetMapping("/findById")
	public DeliveryAddressEntity findById(Long id) {
		return deliveryAddressService.findById(id);
	}

	/**
    * 根据条件查询收货地址列表
    *
    * @param deliveryAddressConditionEntity 条件
    * @return 收货地址列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<DeliveryAddressEntity> searchByPage(@RequestBody DeliveryAddressConditionEntity deliveryAddressConditionEntity) {
		return deliveryAddressService.searchByPage(deliveryAddressConditionEntity);
	}


	/**
     * 添加收货地址
     *
     * @param deliveryAddressEntity 收货地址实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody DeliveryAddressEntity deliveryAddressEntity) {
		return deliveryAddressService.insert(deliveryAddressEntity);
	}

	/**
     * 修改收货地址
     *
     * @param deliveryAddressEntity 收货地址实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody DeliveryAddressEntity deliveryAddressEntity) {
		return deliveryAddressService.update(deliveryAddressEntity);
	}

	/**
     * 批量删除收货地址
     *
     * @param ids 收货地址ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return deliveryAddressService.deleteByIds(ids);
	}
}
