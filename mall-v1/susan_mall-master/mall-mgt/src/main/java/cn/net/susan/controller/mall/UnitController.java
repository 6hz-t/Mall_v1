package cn.net.susan.controller.mall;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.ValidSensitiveWord;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.UnitConditionEntity;
import cn.net.susan.entity.mall.UnitEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.mall.UnitService;
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


@Api(tags = "单位操作", description = "单位接口")
@RestController
@RequestMapping("/v1/unit")
public class UnitController {

	@Autowired
	private UnitService unitService;

	/**
	 * 通过id查询单位信息
	 *
	 * @param id 系统ID
	 * @return 单位信息
	 */
	@ApiOperation(notes = "通过id查询单位信息", value = "通过id查询单位信息")
	@GetMapping("/findById")
	public UnitEntity findById(Long id) {
		return unitService.findById(id);
	}

	/**
    * 根据条件查询单位列表
    *
    * @param unitConditionEntity 条件
    * @return 单位列表
    */
	@ApiOperation(notes = "根据条件查询单位列表", value = "根据条件查询单位列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<UnitEntity> searchByPage(@RequestBody UnitConditionEntity unitConditionEntity) {
		return unitService.searchByPage(unitConditionEntity);
	}


	/**
     * 添加单位
     *
     * @param unitEntity 单位实体
     * @return 影响行数
     */
	@ValidSensitiveWord
	@ApiOperation(notes = "添加单位", value = "添加单位")
	@PostMapping("/insert")
	public int insert(@RequestBody UnitEntity unitEntity) {
		return unitService.insert(unitEntity);
	}

	/**
     * 修改单位
     *
     * @param unitEntity 单位实体
     * @return 影响行数
     */
	@ValidSensitiveWord
	@ApiOperation(notes = "修改单位", value = "修改单位")
	@PostMapping("/update")
	public int update(@RequestBody UnitEntity unitEntity) {
		return unitService.update(unitEntity);
	}

	/**
     * 批量删除单位
     *
     * @param ids 单位ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除单位", value = "批量删除单位")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return unitService.deleteByIds(ids);
	}

	/**
	 * 导出单位数据
	 *
	 * @return 影响行数
	 */
	@ExcelExport(ExcelBizTypeEnum.UNIT)
	@ApiOperation(notes = "导出岗位数据", value = "导出单位数据")
	@PostMapping("/export")
	public void export(@RequestBody UnitConditionEntity unitConditionEntity) {
	}
}
