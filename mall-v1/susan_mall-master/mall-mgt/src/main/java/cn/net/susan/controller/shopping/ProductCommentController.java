package cn.net.susan.controller.shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.shopping.ProductCommentConditionEntity;
import cn.net.susan.entity.shopping.ProductCommentEntity;
import cn.net.susan.service.shopping.ProductCommentService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/productComment")
public class ProductCommentController {

	@Autowired
	private ProductCommentService productCommentService;

	/**
	 * 通过id查询商品评论信息
	 *
	 * @param id 系统ID
	 * @return 商品评论信息
	 */
	@GetMapping("/findById")
	public ProductCommentEntity findById(Long id) {
		return productCommentService.findById(id);
	}

	/**
    * 根据条件查询商品评论列表
    *
    * @param productCommentConditionEntity 条件
    * @return 商品评论列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<ProductCommentEntity> searchByPage(@RequestBody ProductCommentConditionEntity productCommentConditionEntity) {
		return productCommentService.searchByPage(productCommentConditionEntity);
	}


	/**
     * 添加商品评论
     *
     * @param productCommentEntity 商品评论实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody ProductCommentEntity productCommentEntity) {
		return productCommentService.insert(productCommentEntity);
	}

	/**
     * 修改商品评论
     *
     * @param productCommentEntity 商品评论实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody ProductCommentEntity productCommentEntity) {
		return productCommentService.update(productCommentEntity);
	}

	/**
     * 批量删除商品评论
     *
     * @param ids 商品评论ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return productCommentService.deleteByIds(ids);
	}
}
