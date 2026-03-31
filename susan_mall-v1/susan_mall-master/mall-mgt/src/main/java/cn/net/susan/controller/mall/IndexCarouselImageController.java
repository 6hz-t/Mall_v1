package cn.net.susan.controller.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.IndexCarouselImageConditionEntity;
import cn.net.susan.entity.mall.IndexCarouselImageEntity;
import cn.net.susan.service.mall.IndexCarouselImageService;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;


@Api(tags = "首页轮播图操作", description = "首页轮播图接口")
@RestController
@RequestMapping("/v1/indexCarouselImage")
public class IndexCarouselImageController {

	@Autowired
	private IndexCarouselImageService indexCarouselImageService;

	/**
	 * 通过id查询首页轮播图信息
	 *
	 * @param id 系统ID
	 * @return 首页轮播图信息
	 */
	@ApiOperation(notes = "通过id查询首页轮播图信息", value = "通过id查询首页轮播图信息")
	@GetMapping("/findById")
	public IndexCarouselImageEntity findById(Long id) {
		return indexCarouselImageService.findById(id);
	}

	/**
    * 根据条件查询首页轮播图列表
    *
    * @param indexCarouselImageConditionEntity 条件
    * @return 首页轮播图列表
    */
	@ApiOperation(notes = "根据条件查询首页轮播图列表", value = "根据条件查询首页轮播图列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<IndexCarouselImageEntity> searchByPage(@RequestBody IndexCarouselImageConditionEntity indexCarouselImageConditionEntity) {
		return indexCarouselImageService.searchByPage(indexCarouselImageConditionEntity);
	}


	/**
     * 添加首页轮播图
     *
     * @param indexCarouselImageEntity 首页轮播图实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加首页轮播图", value = "添加首页轮播图")
	@PostMapping("/insert")
	public int insert(@RequestBody IndexCarouselImageEntity indexCarouselImageEntity) {
		return indexCarouselImageService.insert(indexCarouselImageEntity);
	}

	/**
     * 修改首页轮播图
     *
     * @param indexCarouselImageEntity 首页轮播图实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改首页轮播图", value = "修改首页轮播图")
	@PostMapping("/update")
	public int update(@RequestBody IndexCarouselImageEntity indexCarouselImageEntity) {
		return indexCarouselImageService.update(indexCarouselImageEntity);
	}

	/**
     * 批量删除首页轮播图
     *
     * @param ids 首页轮播图ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除首页轮播图", value = "批量删除首页轮播图")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return indexCarouselImageService.deleteByIds(ids);
	}
}
