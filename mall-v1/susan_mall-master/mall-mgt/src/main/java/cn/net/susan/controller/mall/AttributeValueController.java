package cn.net.susan.controller.mall;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.ValidSensitiveWord;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.AttributeValueConditionEntity;
import cn.net.susan.entity.mall.AttributeValueEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.mall.AttributeValueService;
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


@Api(tags = "属性值操作", description = "属性值接口")
@RestController
@RequestMapping("/v1/attributeValue")
public class AttributeValueController {

    @Autowired
    private AttributeValueService attributeValueService;

    /**
     * 通过id查询属性值信息
     *
     * @param id 系统ID
     * @return 属性值信息
     */
    @ApiOperation(notes = "通过id查询属性值信息", value = "通过id查询属性值信息")
    @GetMapping("/findById")
    public AttributeValueEntity findById(Long id) {
        return attributeValueService.findById(id);
    }

    /**
     * 根据条件查询属性值列表
     *
     * @param attributeValueConditionEntity 条件
     * @return 属性值列表
     */
    @ApiOperation(notes = "根据条件查询属性值列表", value = "根据条件查询属性值列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<AttributeValueEntity> searchByPage(@RequestBody AttributeValueConditionEntity attributeValueConditionEntity) {
        return attributeValueService.searchByPage(attributeValueConditionEntity);
    }


    /**
     * 添加属性值
     *
     * @param attributeValueEntity 属性值实体
     * @return 影响行数
     */
    @ValidSensitiveWord
    @ApiOperation(notes = "添加属性值", value = "添加属性值")
    @PostMapping("/insert")
    public int insert(@RequestBody AttributeValueEntity attributeValueEntity) {
        return attributeValueService.insert(attributeValueEntity);
    }

    /**
     * 修改属性值
     *
     * @param attributeValueEntity 属性值实体
     * @return 影响行数
     */
    @ValidSensitiveWord
    @ApiOperation(notes = "修改属性值", value = "修改属性值")
    @PostMapping("/update")
    public int update(@RequestBody AttributeValueEntity attributeValueEntity) {
        return attributeValueService.update(attributeValueEntity);
    }

    /**
     * 批量删除属性值
     *
     * @param ids 属性值ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除属性值", value = "批量删除属性值")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return attributeValueService.deleteByIds(ids);
    }

    /**
     * 导出属性值数据
     *
     * @return 影响行数
     */
    @ExcelExport(ExcelBizTypeEnum.ATTRIBUTE_VALUE)
    @ApiOperation(notes = "导出属性值数据", value = "导出属性值数据")
    @PostMapping("/export")
    public void export(@RequestBody AttributeValueConditionEntity attributeValueConditionEntity) {
    }
}
