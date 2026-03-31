package cn.net.susan.controller.mall;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.ValidSensitiveWord;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.mall.ProductService;
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


@Api(tags = "商品操作", description = "商品接口")
@RestController
@RequestMapping("/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 通过id查询商品信息
     *
     * @param id 系统ID
     * @return 商品信息
     */
    @ApiOperation(notes = "通过id查询商品信息", value = "通过id查询商品信息")
    @GetMapping("/findById")
    public ProductEntity findById(Long id) {
        return productService.findById(id);
    }

    /**
     * 根据条件查询商品列表
     *
     * @param productConditionEntity 条件
     * @return 商品列表
     */
    @ApiOperation(notes = "根据条件查询商品列表", value = "根据条件查询商品列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<ProductEntity> searchByPage(@RequestBody ProductConditionEntity productConditionEntity) {
        return productService.searchByPage(productConditionEntity);
    }

    /**
     * 新增商品
     *
     * @param productEntity 商品实体
     * @return 影响行数
     */
    @ValidSensitiveWord
    @ApiOperation(notes = "新增商品", value = "新增商品")
    @PostMapping("/insert")
    public void insert(@RequestBody ProductEntity productEntity) {
        productService.insert(productEntity);
    }

    /**
     * 批量创建商品
     *
     * @param productEntityList 批量创建商品
     * @return 影响行数
     */
    @ValidSensitiveWord
    @ApiOperation(notes = "批量创建商品", value = "批量创建商品")
    @PostMapping("/generate")
    public List<ProductEntity> generate(@RequestBody List<ProductEntity> productEntityList) {
        return productService.generate(productEntityList);
    }

    /**
     * 修改商品
     *
     * @param productEntity 商品实体
     * @return 影响行数
     */
    @ValidSensitiveWord
    @ApiOperation(notes = "修改商品", value = "修改商品")
    @PostMapping("/update")
    public void update(@RequestBody ProductEntity productEntity) {
        productService.update(productEntity);
    }

    /**
     * 批量删除商品
     *
     * @param ids 商品ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除商品", value = "批量删除商品")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return productService.deleteByIds(ids);
    }

    /**
     * 导出商品数据
     *
     * @return 影响行数
     */
    @ExcelExport(ExcelBizTypeEnum.PRODUCT)
    @ApiOperation(notes = "导出商品数据", value = "导出商品数据")
    @PostMapping("/export")
    public void export(@RequestBody ProductConditionEntity productConditionEntity) {
    }
}
