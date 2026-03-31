package cn.net.susan.controller.seckill;

import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.seckill.SeckillProductConditionEntity;
import cn.net.susan.entity.seckill.SeckillProductEntity;
import cn.net.susan.service.seckill.SeckillProductService;
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


@Api(tags = "秒杀商品操作", description = "秒杀商品接口")
@RestController
@RequestMapping("/v1/seckillProduct")
public class SeckillProductController {

	@Autowired
	private SeckillProductService seckillProductService;

	/**
	 * 通过id查询秒杀商品信息
	 *
	 * @param id 系统ID
	 * @return 秒杀商品信息
	 */
	@ApiOperation(notes = "通过id查询秒杀商品信息", value = "通过id查询秒杀商品信息")
	@GetMapping("/findById")
	public SeckillProductEntity findById(Long id) {
		return seckillProductService.findById(id);
	}

	/**
    * 根据条件查询秒杀商品列表
    *
    * @param seckillProductConditionEntity 条件
    * @return 秒杀商品列表
    */
	@ApiOperation(notes = "根据条件查询秒杀商品列表", value = "根据条件查询秒杀商品列表")
	@PostMapping("/searchByPage")
	public ResponsePageEntity<SeckillProductEntity> searchByPage(@RequestBody SeckillProductConditionEntity seckillProductConditionEntity) {
		return seckillProductService.searchByPage(seckillProductConditionEntity);
	}


	/**
     * 添加秒杀商品
     *
     * @param seckillProductEntity 秒杀商品实体
     * @return 影响行数
     */
	@ApiOperation(notes = "添加秒杀商品", value = "添加秒杀商品")
	@PostMapping("/insert")
	public void insert(@RequestBody SeckillProductEntity seckillProductEntity) {
		 seckillProductService.insert(seckillProductEntity);
	}

	/**
     * 修改秒杀商品
     *
     * @param seckillProductEntity 秒杀商品实体
     * @return 影响行数
     */
	@ApiOperation(notes = "修改秒杀商品", value = "修改秒杀商品")
	@PostMapping("/update")
	public void update(@RequestBody SeckillProductEntity seckillProductEntity) {
		seckillProductService.update(seckillProductEntity);
	}

	/**
     * 批量删除秒杀商品
     *
     * @param ids 秒杀商品ID集合
     * @return 影响行数
     */
	@ApiOperation(notes = "批量删除秒杀商品", value = "批量删除秒杀商品")
	@PostMapping("/deleteByIds")
	public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
		return seckillProductService.deleteByIds(ids);
	}
}
