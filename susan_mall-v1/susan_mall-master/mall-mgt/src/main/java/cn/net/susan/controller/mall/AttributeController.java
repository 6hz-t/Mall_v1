package cn.net.susan.controller.mall;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.ValidSensitiveWord;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.AttributeConditionEntity;
import cn.net.susan.entity.mall.AttributeEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.mall.AttributeService;
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


@Api(tags = "属性操作", description = "属性接口")
@RestController
@RequestMapping("/v1/attribute")
public class AttributeController {

	@Autowired
	private AttributeService attributeService;

	/**
	 * 通过id查询属性信息
	 *
	 * @param id 系统ID
	 * @return 属性信息
	 */
	@ApiOperation(notes = "通过id查询属性信息", value = "通过id查询属性信息")
	@GetMapping("/findById")
	public AttributeEntity findById(Long id) {
		return attributeService.findById(id);
	}

	/**
    * 根据条件查询属性列表
    *
    * @param attributeConditionEntity 条件
    * @return 属性列表
    */
	@ApiOperation(notes = "根据条件查询属性列表", value = "根据条件查询属性列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<AttributeEntity> searchByPage(@RequestBody AttributeConditionEntity attributeConditionEntity) {
		return attributeService.searchByPage(attributeConditionEntity);
	}


	/**
     * 添加属性
     *
     * @param attributeEntity 属性实体
     * @return 影响行数
     */
	@ValidSensitiveWord
	@ApiOperation(notes = "添加属性", value = "添加属性")
	@PostMapping("/insert")
	public int insert(@RequestBody AttributeEntity attributeEntity) {
		return attributeService.insert(attributeEntity);
	}

	/**
     * 修改属性
     *
     * @param attributeEntity 属性实体
     * @return 影响行数
     */
	@ValidSensitiveWord
	@ApiOperation(notes = "修改属性", value = "修改属性")
	@PostMapping("/update")
	public int update(@RequestBody AttributeEntity attributeEntity) {
		return attributeService.update(attributeEntity);
	}

	/**
     * 批量删除属性
     *
     * @param ids 属性ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除属性", value = "批量删除属性")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return attributeService.deleteByIds(ids);
	}

	/**
	 * 导出属性数据
	 *
	 * @return 影响行数
	 */
	@ExcelExport(ExcelBizTypeEnum.ATTRIBUTE)
	@ApiOperation(notes = "导出属性数据", value = "导出属性数据")
	@PostMapping("/export")
	public void export(@RequestBody AttributeConditionEntity attributeConditionEntity) {
	}
}
