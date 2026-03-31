package cn.net.susan.controller.mall;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.ValidSensitiveWord;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.BrandConditionEntity;
import cn.net.susan.entity.mall.BrandEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.mall.BrandService;
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


@Api(tags = "品牌操作", description = "品牌接口")
@RestController
@RequestMapping("/v1/brand")
public class BrandController {

	@Autowired
	private BrandService brandService;

	/**
	 * 通过id查询品牌信息
	 *
	 * @param id 系统ID
	 * @return 品牌信息
	 */
	@ApiOperation(notes = "通过id查询品牌信息", value = "通过id查询品牌信息")
	@GetMapping("/findById")
	public BrandEntity findById(Long id) {
		return brandService.findById(id);
	}

	/**
    * 根据条件查询品牌列表
    *
    * @param brandConditionEntity 条件
    * @return 品牌列表
    */
	@ApiOperation(notes = "根据条件查询品牌列表", value = "根据条件查询品牌列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<BrandEntity> searchByPage(@RequestBody BrandConditionEntity brandConditionEntity) {
		return brandService.searchByPage(brandConditionEntity);
	}


	/**
     * 添加品牌
     *
     * @param brandEntity 品牌实体
     * @return 影响行数
     */
	@ValidSensitiveWord
	@ApiOperation(notes = "添加品牌", value = "添加品牌")
	@PostMapping("/insert")
	public int insert(@RequestBody BrandEntity brandEntity) {
		return brandService.insert(brandEntity);
	}

	/**
     * 修改品牌
     *
     * @param brandEntity 品牌实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改品牌", value = "修改品牌")
	@PostMapping("/update")
	public int update(@RequestBody BrandEntity brandEntity) {
		return brandService.update(brandEntity);
	}

	/**
     * 批量删除品牌
     *
     * @param ids 品牌ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除品牌", value = "批量删除品牌")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return brandService.deleteByIds(ids);
	}

	/**
	 * 导出品牌数据
	 *
	 * @return 影响行数
	 */
	@ExcelExport(ExcelBizTypeEnum.BRAND)
	@ApiOperation(notes = "导出品牌数据", value = "导出品牌数据")
	@PostMapping("/export")
	public void export(@RequestBody BrandConditionEntity brandConditionEntity) {
	}
}
