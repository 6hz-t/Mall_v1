package cn.net.susan.controller.shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.shopping.ShoppingCartConditionEntity;
import cn.net.susan.entity.shopping.ShoppingCartEntity;
import cn.net.susan.service.shopping.ShoppingCartService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	/**
	 * 通过id查询购物车信息
	 *
	 * @param id 系统ID
	 * @return 购物车信息
	 */
	@GetMapping("/findById")
	public ShoppingCartEntity findById(Long id) {
		return shoppingCartService.findById(id);
	}

	/**
    * 根据条件查询购物车列表
    *
    * @param shoppingCartConditionEntity 条件
    * @return 购物车列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<ShoppingCartEntity> searchByPage(@RequestBody ShoppingCartConditionEntity shoppingCartConditionEntity) {
		return shoppingCartService.searchByPage(shoppingCartConditionEntity);
	}


	/**
     * 添加购物车
     *
     * @param shoppingCartEntity 购物车实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody ShoppingCartEntity shoppingCartEntity) {
		return shoppingCartService.insert(shoppingCartEntity);
	}

	/**
     * 修改购物车
     *
     * @param shoppingCartEntity 购物车实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody ShoppingCartEntity shoppingCartEntity) {
		return shoppingCartService.update(shoppingCartEntity);
	}

	/**
     * 批量删除购物车
     *
     * @param ids 购物车ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return shoppingCartService.deleteByIds(ids);
	}
}
