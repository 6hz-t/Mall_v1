package cn.net.susan.controller.common;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.entity.common.CommonPhotoConditionEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.common.CommonPhotoGroupConditionEntity;
import cn.net.susan.entity.common.CommonPhotoGroupEntity;
import cn.net.susan.service.common.CommonPhotoGroupService;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;


@Api(tags = "图片分组操作", description = "图片分组接口")
@RestController
@RequestMapping("/v1/commonPhotoGroup")
public class CommonPhotoGroupController {

    @Autowired
    private CommonPhotoGroupService commonPhotoGroupService;

    /**
     * 通过id查询图片分组信息
     *
     * @param id 系统ID
     * @return 图片分组信息
     */
    @ApiOperation(notes = "通过id查询图片分组信息", value = "通过id查询图片分组信息")
    @GetMapping("/findById")
    public CommonPhotoGroupEntity findById(Long id) {
        return commonPhotoGroupService.findById(id);
    }

    /**
     * 根据条件查询图片分组列表
     *
     * @param commonPhotoGroupConditionEntity 条件
     * @return 图片分组列表
     */
    @ApiOperation(notes = "根据条件查询图片分组列表", value = "根据条件查询图片分组列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<CommonPhotoGroupEntity> searchByPage(@RequestBody CommonPhotoGroupConditionEntity commonPhotoGroupConditionEntity) {
        return commonPhotoGroupService.searchByPage(commonPhotoGroupConditionEntity);
    }


    /**
     * 添加图片分组
     *
     * @param commonPhotoGroupEntity 图片分组实体
     * @return 影响行数
     */
    @ApiOperation(notes = "添加图片分组", value = "添加图片分组")
    @PostMapping("/insert")
    public int insert(@RequestBody CommonPhotoGroupEntity commonPhotoGroupEntity) {
        return commonPhotoGroupService.insert(commonPhotoGroupEntity);
    }

    /**
     * 修改图片分组
     *
     * @param commonPhotoGroupEntity 图片分组实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改图片分组", value = "修改图片分组")
    @PostMapping("/update")
    public int update(@RequestBody CommonPhotoGroupEntity commonPhotoGroupEntity) {
        return commonPhotoGroupService.update(commonPhotoGroupEntity);
    }

    /**
     * 批量删除图片分组
     *
     * @param ids 图片分组ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除图片分组", value = "批量删除图片分组")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return commonPhotoGroupService.deleteByIds(ids);
    }

    /**
     * 导出图片组数据
     *
     * @return 影响行数
     */
    @ExcelExport(ExcelBizTypeEnum.COMMON_PHOTO_GROUP)
    @ApiOperation(notes = "导出图片组数据", value = "导出图片组数据")
    @PostMapping("/export")
    public void export(@RequestBody CommonPhotoGroupConditionEntity commonPhotoGroupConditionEntity) {
    }
}
