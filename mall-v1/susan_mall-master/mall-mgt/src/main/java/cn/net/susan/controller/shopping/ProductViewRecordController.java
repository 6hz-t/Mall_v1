package cn.net.susan.controller.shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.shopping.ProductViewRecordConditionEntity;
import cn.net.susan.entity.shopping.ProductViewRecordEntity;
import cn.net.susan.service.shopping.ProductViewRecordService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/productViewRecord")
public class ProductViewRecordController {

	@Autowired
	private ProductViewRecordService productViewRecordService;

	/**
	 * 通过id查询商品浏览记录信息
	 *
	 * @param id 系统ID
	 * @return 商品浏览记录信息
	 */
	@GetMapping("/findById")
	public ProductViewRecordEntity findById(Long id) {
		return productViewRecordService.findById(id);
	}

	/**
    * 根据条件查询商品浏览记录列表
    *
    * @param productViewRecordConditionEntity 条件
    * @return 商品浏览记录列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<ProductViewRecordEntity> searchByPage(@RequestBody ProductViewRecordConditionEntity productViewRecordConditionEntity) {
		return productViewRecordService.searchByPage(productViewRecordConditionEntity);
	}


	/**
     * 添加商品浏览记录
     *
     * @param productViewRecordEntity 商品浏览记录实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody ProductViewRecordEntity productViewRecordEntity) {
		return productViewRecordService.insert(productViewRecordEntity);
	}

	/**
     * 修改商品浏览记录
     *
     * @param productViewRecordEntity 商品浏览记录实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody ProductViewRecordEntity productViewRecordEntity) {
		return productViewRecordService.update(productViewRecordEntity);
	}

	/**
     * 批量删除商品浏览记录
     *
     * @param ids 商品浏览记录ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return productViewRecordService.deleteByIds(ids);
	}
}
