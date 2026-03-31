package cn.net.susan.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.common.CommonSmsRecordConditionEntity;
import cn.net.susan.entity.common.CommonSmsRecordEntity;
import cn.net.susan.service.common.CommonSmsRecordService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/commonSmsRecord")
public class CommonSmsRecordController {

	@Autowired
	private CommonSmsRecordService commonSmsRecordService;

	/**
	 * 通过id查询短信发送记录信息
	 *
	 * @param id 系统ID
	 * @return 短信发送记录信息
	 */
	@GetMapping("/findById")
	public CommonSmsRecordEntity findById(Long id) {
		return commonSmsRecordService.findById(id);
	}

	/**
    * 根据条件查询短信发送记录列表
    *
    * @param commonSmsRecordConditionEntity 条件
    * @return 短信发送记录列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<CommonSmsRecordEntity> searchByPage(@RequestBody CommonSmsRecordConditionEntity commonSmsRecordConditionEntity) {
		return commonSmsRecordService.searchByPage(commonSmsRecordConditionEntity);
	}


	/**
     * 添加短信发送记录
     *
     * @param commonSmsRecordEntity 短信发送记录实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody CommonSmsRecordEntity commonSmsRecordEntity) {
		return commonSmsRecordService.insert(commonSmsRecordEntity);
	}

	/**
     * 修改短信发送记录
     *
     * @param commonSmsRecordEntity 短信发送记录实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody CommonSmsRecordEntity commonSmsRecordEntity) {
		return commonSmsRecordService.update(commonSmsRecordEntity);
	}

	/**
     * 批量删除短信发送记录
     *
     * @param ids 短信发送记录ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return commonSmsRecordService.deleteByIds(ids);
	}
}
