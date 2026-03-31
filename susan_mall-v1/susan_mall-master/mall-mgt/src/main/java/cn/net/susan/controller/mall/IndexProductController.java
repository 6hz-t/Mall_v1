package cn.net.susan.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.IndexProductConditionEntity;
import cn.net.susan.entity.mall.IndexProductEntity;
import cn.net.susan.service.mall.IndexProductService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;


@Api(tags = "首页商品操作", description = "首页商品接口")
@RestController
@RequestMapping("/v1/indexProduct")
public class IndexProductController {

	@Autowired
	private IndexProductService indexProductService;

	/**
	 * 通过id查询首页商品信息
	 *
	 * @param id 系统ID
	 * @return 首页商品信息
	 */
	@ApiOperation(notes = "通过id查询首页商品信息", value = "通过id查询首页商品信息")
	@GetMapping("/findById")
	public IndexProductEntity findById(Long id) {
		return indexProductService.findById(id);
	}

	/**
    * 根据条件查询首页商品列表
    *
    * @param indexProductConditionEntity 条件
    * @return 首页商品列表
    */
	@ApiOperation(notes = "根据条件查询首页商品列表", value = "根据条件查询首页商品列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<IndexProductEntity> searchByPage(@RequestBody IndexProductConditionEntity indexProductConditionEntity) {
		return indexProductService.searchByPage(indexProductConditionEntity);
	}


	/**
     * 添加首页商品
     *
     * @param indexProductEntity 首页商品实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加首页商品", value = "添加首页商品")
	@PostMapping("/insert")
	public int insert(@RequestBody IndexProductEntity indexProductEntity) {
		return indexProductService.insert(indexProductEntity);
	}

	/**
     * 修改首页商品
     *
     * @param indexProductEntity 首页商品实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改首页商品", value = "修改首页商品")
	@PostMapping("/update")
	public int update(@RequestBody IndexProductEntity indexProductEntity) {
		return indexProductService.update(indexProductEntity);
	}

	/**
     * 批量删除首页商品
     *
     * @param ids 首页商品ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除首页商品", value = "批量删除首页商品")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return indexProductService.deleteByIds(ids);
	}
}
