package cn.net.susan.controller.common;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.common.CommonJobConditionEntity;
import cn.net.susan.entity.common.CommonJobEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.common.CommonJobService;
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


@Api(tags = "定时任务操作", description = "定时任务接口")
@RestController
@RequestMapping("/v1/commonJob")
public class CommonJobController {

    @Autowired
    private CommonJobService commonJobService;

    /**
     * 通过id查询定时任务信息
     *
     * @param id 系统ID
     * @return 定时任务信息
     */
    @ApiOperation(notes = "通过id查询定时任务信息", value = "通过id查询定时任务信息")
    @GetMapping("/findById")
    public CommonJobEntity findById(Long id) {
        return commonJobService.findById(id);
    }

    /**
     * 根据条件查询定时任务列表
     *
     * @param commonJobConditionEntity 条件
     * @return 定时任务列表
     */
    @ApiOperation(notes = "根据条件查询定时任务列表", value = "根据条件查询定时任务列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<CommonJobEntity> searchByPage(@RequestBody CommonJobConditionEntity commonJobConditionEntity) {
        return commonJobService.searchByPage(commonJobConditionEntity);
    }


    /**
     * 添加定时任务
     *
     * @param commonJobEntity 定时任务实体
     * @return 影响行数
     */
    @ApiOperation(notes = "添加定时任务", value = "添加定时任务")
    @PostMapping("/insert")
    public void insert(@RequestBody CommonJobEntity commonJobEntity) {
        commonJobService.insert(commonJobEntity);
    }

    /**
     * 修改定时任务
     *
     * @param commonJobEntity 定时任务实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改定时任务", value = "修改定时任务")
    @PostMapping("/update")
    public int update(@RequestBody CommonJobEntity commonJobEntity) {
        return commonJobService.update(commonJobEntity);
    }

    /**
     * 批量删除定时任务
     *
     * @param ids 定时任务ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除定时任务", value = "批量删除定时任务")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return commonJobService.deleteByIds(ids);
    }


    /**
     * 立即执行定时任务
     *
     * @param commonJobEntity 定时任务实体
     * @return 影响行数
     */
    @ApiOperation(notes = "立即执行定时任务", value = "立即执行定时任务")
    @PostMapping("/runNow")
    public void runNow(@RequestBody CommonJobEntity commonJobEntity) {
        commonJobService.runNow(commonJobEntity);
    }

    /**
     * 暂停定时任务
     *
     * @param commonJobEntity 定时任务实体
     * @return 影响行数
     */
    @ApiOperation(notes = "暂停定时任务", value = "暂停定时任务")
    @PostMapping("/pause")
    public void pause(@RequestBody CommonJobEntity commonJobEntity) {
        commonJobService.pause(commonJobEntity);
    }


    /**
     * 恢复定时任务
     *
     * @param commonJobEntity 定时任务实体
     * @return 影响行数
     */
    @ApiOperation(notes = "恢复定时任务", value = "恢复定时任务")
    @PostMapping("/resume")
    public void resume(@RequestBody CommonJobEntity commonJobEntity) {
        commonJobService.resume(commonJobEntity);
    }

    /**
     * 导出定时任务数据
     *
     * @return 影响行数
     */
    @ExcelExport(ExcelBizTypeEnum.COMMON_JOB)
    @ApiOperation(notes = "导出定时任务数据", value = "导出定时任务数据")
    @PostMapping("/export")
    public void export(@RequestBody CommonJobConditionEntity commonJobConditionEntity) {
    }
}
