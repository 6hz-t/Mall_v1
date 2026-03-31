package cn.net.susan.controller.mall;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.NoLogin;
import cn.net.susan.annotation.ValidSensitiveWord;
import cn.net.susan.dto.mall.CategoryTreeDTO;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.CategoryConditionEntity;
import cn.net.susan.entity.mall.CategoryEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.mall.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = "分类操作", description = "分类接口")
@RestController
@RequestMapping("/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * 通过id查询分类信息
	 *
	 * @param id 系统ID
	 * @return 分类信息
	 */
	@ApiOperation(notes = "通过id查询分类信息", value = "通过id查询分类信息")
	@GetMapping("/findById")
	public CategoryEntity findById(Long id) {
		return categoryService.findById(id);
	}

	/**
    * 根据条件查询分类列表
    *
    * @param categoryConditionEntity 条件
    * @return 分类列表
    */
	@ApiOperation(notes = "根据条件查询分类列表", value = "根据条件查询分类列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<CategoryEntity> searchByPage(@RequestBody CategoryConditionEntity categoryConditionEntity) {
		return categoryService.searchByPage(categoryConditionEntity);
	}


	/**
     * 添加分类
     *
     * @param categoryEntity 分类实体
     * @return 影响行数
     */
	@ValidSensitiveWord
	@ApiOperation(notes = "添加分类", value = "添加分类")
	@PostMapping("/insert")
	public void insert(@RequestBody CategoryEntity categoryEntity) {
		categoryService.insert(categoryEntity);
	}

	/**
     * 修改分类
     *
     * @param categoryEntity 分类实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改分类", value = "修改分类")
	@PostMapping("/update")
	public int update(@RequestBody CategoryEntity categoryEntity) {
		return categoryService.update(categoryEntity);
	}

	/**
     * 批量删除分类
     *
     * @param ids 分类ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除分类", value = "批量删除分类")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return categoryService.deleteByIds(ids);
	}

	/**
	 * 查询分类树
	 *
	 * @param categoryConditionEntity 条件
	 * @return 分类树
	 */
	@NoLogin
	@ApiOperation(notes = "查询分类树", value = "查询分类树")
	@PostMapping("/searchByTree")
	public List<CategoryTreeDTO> searchByTree(@RequestBody CategoryConditionEntity categoryConditionEntity) {
		return categoryService.searchByTree(categoryConditionEntity);
	}

	/**
	 * 导出分类数据
	 *
	 * @return 影响行数
	 */
	@ExcelExport(ExcelBizTypeEnum.CATEGORY)
	@ApiOperation(notes = "导出分类数据", value = "导出分类数据")
	@PostMapping("/export")
	public void export(@RequestBody CategoryConditionEntity categoryConditionEntity) {
	}
}
