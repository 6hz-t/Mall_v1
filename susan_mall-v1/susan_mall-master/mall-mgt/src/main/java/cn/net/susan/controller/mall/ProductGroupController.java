package cn.net.susan.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.ProductGroupConditionEntity;
import cn.net.susan.entity.mall.ProductGroupEntity;
import cn.net.susan.service.mall.ProductGroupService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/productGroup")
public class ProductGroupController {

	@Autowired
	private ProductGroupService productGroupService;

	/**
	 * 通过id查询商品组信息
	 *
	 * @param id 系统ID
	 * @return 商品组信息
	 */
	@GetMapping("/findById")
	public ProductGroupEntity findById(Long id) {
		return productGroupService.findById(id);
	}

	/**
    * 根据条件查询商品组列表
    *
    * @param productGroupConditionEntity 条件
    * @return 商品组列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<ProductGroupEntity> searchByPage(@RequestBody ProductGroupConditionEntity productGroupConditionEntity) {
		return productGroupService.searchByPage(productGroupConditionEntity);
	}


	/**
     * 添加商品组
     *
     * @param productGroupEntity 商品组实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody ProductGroupEntity productGroupEntity) {
		return productGroupService.insert(productGroupEntity);
	}

	/**
     * 修改商品组
     *
     * @param productGroupEntity 商品组实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody ProductGroupEntity productGroupEntity) {
		return productGroupService.update(productGroupEntity);
	}

	/**
     * 批量删除商品组
     *
     * @param ids 商品组ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return productGroupService.deleteByIds(ids);
	}
}
