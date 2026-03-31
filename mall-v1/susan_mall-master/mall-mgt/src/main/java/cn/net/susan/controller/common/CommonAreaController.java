package cn.net.susan.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.common.CommonAreaConditionEntity;
import cn.net.susan.entity.common.CommonAreaEntity;
import cn.net.susan.service.common.CommonAreaService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/commonArea")
public class CommonAreaController {

	@Autowired
	private CommonAreaService commonAreaService;

	/**
	 * 通过id查询地区信息
	 *
	 * @param id 系统ID
	 * @return 地区信息
	 */
	@GetMapping("/findById")
	public CommonAreaEntity findById(Long id) {
		return commonAreaService.findById(id);
	}

	/**
    * 根据条件查询地区列表
    *
    * @param commonAreaConditionEntity 条件
    * @return 地区列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<CommonAreaEntity> searchByPage(@RequestBody CommonAreaConditionEntity commonAreaConditionEntity) {
		return commonAreaService.searchByPage(commonAreaConditionEntity);
	}


	/**
     * 添加地区
     *
     * @param commonAreaEntity 地区实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody CommonAreaEntity commonAreaEntity) {
		return commonAreaService.insert(commonAreaEntity);
	}

	/**
     * 修改地区
     *
     * @param commonAreaEntity 地区实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody CommonAreaEntity commonAreaEntity) {
		return commonAreaService.update(commonAreaEntity);
	}

	/**
     * 批量删除地区
     *
     * @param ids 地区ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return commonAreaService.deleteByIds(ids);
	}
}
