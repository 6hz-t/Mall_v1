package cn.net.susan.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.IndexNoticeConditionEntity;
import cn.net.susan.entity.mall.IndexNoticeEntity;
import cn.net.susan.service.mall.IndexNoticeService;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/indexNotice")
public class IndexNoticeController {

	@Autowired
	private IndexNoticeService indexNoticeService;

	/**
	 * 通过id查询首页公告信息
	 *
	 * @param id 系统ID
	 * @return 首页公告信息
	 */
	@GetMapping("/findById")
	public IndexNoticeEntity findById(Long id) {
		return indexNoticeService.findById(id);
	}

	/**
    * 根据条件查询首页公告列表
    *
    * @param indexNoticeConditionEntity 条件
    * @return 首页公告列表
    */
	@PostMapping("/searchByPage")
	public ResponsePageEntity<IndexNoticeEntity> searchByPage(@RequestBody IndexNoticeConditionEntity indexNoticeConditionEntity) {
		return indexNoticeService.searchByPage(indexNoticeConditionEntity);
	}


	/**
     * 添加首页公告
     *
     * @param indexNoticeEntity 首页公告实体
     * @return 影响行数
     */
	@PostMapping("/insert")
	public int insert(@RequestBody IndexNoticeEntity indexNoticeEntity) {
		return indexNoticeService.insert(indexNoticeEntity);
	}

	/**
     * 修改首页公告
     *
     * @param indexNoticeEntity 首页公告实体
     * @return 影响行数
     */
	@PostMapping("/update")
	public int update(@RequestBody IndexNoticeEntity indexNoticeEntity) {
		return indexNoticeService.update(indexNoticeEntity);
	}

	/**
     * 批量删除首页公告
     *
     * @param ids 首页公告ID集合
     * @return 影响行数
     */
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return indexNoticeService.deleteByIds(ids);
	}
}
