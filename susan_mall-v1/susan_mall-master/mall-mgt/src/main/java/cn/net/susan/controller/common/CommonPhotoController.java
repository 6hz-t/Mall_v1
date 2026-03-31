package cn.net.susan.controller.common;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.common.CommonPhotoConditionEntity;
import cn.net.susan.entity.common.CommonPhotoEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.common.CommonPhotoService;
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
import java.util.Objects;


@Api(tags = "图片操作", description = "图片接口")
@RestController
@RequestMapping("/v1/commonPhoto")
public class CommonPhotoController {

	@Autowired
	private CommonPhotoService commonPhotoService;

	/**
	 * 通过id查询图片信息
	 *
	 * @param id 系统ID
	 * @return 图片信息
	 */
	@ApiOperation(notes = "通过id查询图片信息", value = "通过id查询图片信息")
	@GetMapping("/findById")
	public CommonPhotoEntity findById(Long id) {
		return commonPhotoService.findById(id);
	}

	/**
    * 根据条件查询图片列表
    *
    * @param commonPhotoConditionEntity 条件
    * @return 图片列表
    */
	@ApiOperation(notes = "根据条件查询图片列表", value = "根据条件查询图片列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<CommonPhotoEntity> searchByPage(@RequestBody CommonPhotoConditionEntity commonPhotoConditionEntity) {
		return commonPhotoService.searchByPage(commonPhotoConditionEntity);
	}


	/**
     * 添加图片
     *
     * @param commonPhotoEntity 图片实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加图片", value = "添加图片")
	@PostMapping("/insert")
	public int insert(@RequestBody CommonPhotoEntity commonPhotoEntity) {
		if(Objects.isNull(commonPhotoEntity.getPhotoGroupId())) {
			//默认分组
			commonPhotoEntity.setPhotoGroupId(0L);
		}
		return commonPhotoService.insert(commonPhotoEntity);
	}

	/**
     * 修改图片
     *
     * @param commonPhotoEntity 图片实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改图片", value = "修改图片")
	@PostMapping("/update")
	public int update(@RequestBody CommonPhotoEntity commonPhotoEntity) {
		return commonPhotoService.update(commonPhotoEntity);
	}

	/**
     * 批量删除图片
     *
     * @param ids 图片ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除图片", value = "批量删除图片")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return commonPhotoService.deleteByIds(ids);
	}
}
