package cn.net.susan.controller.afterSale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.aftersale.RefundConditionEntity;
import cn.net.susan.entity.aftersale.RefundEntity;
import cn.net.susan.service.aftersale.RefundService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/refund")
public class RefundController {

	@Autowired
	private RefundService refundService;

	/**
	 * 通过id查询退货单信息
	 *
	 * @param id 系统ID
	 * @return 退货单信息
	 */
	@GetMapping("/findById")
	public RefundEntity findById(Long id) {
		return refundService.findById(id);
	}

	/**
    * 根据条件查询退货单列表
    *
    * @param refundConditionEntity 条件
    * @return 退货单列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<RefundEntity> searchByPage(@RequestBody RefundConditionEntity refundConditionEntity) {
		return refundService.searchByPage(refundConditionEntity);
	}


	/**
     * 添加退货单
     *
     * @param refundEntity 退货单实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody RefundEntity refundEntity) {
		return refundService.insert(refundEntity);
	}

	/**
     * 修改退货单
     *
     * @param refundEntity 退货单实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody RefundEntity refundEntity) {
		return refundService.update(refundEntity);
	}

	/**
     * 批量删除退货单
     *
     * @param ids 退货单ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return refundService.deleteByIds(ids);
	}
}
