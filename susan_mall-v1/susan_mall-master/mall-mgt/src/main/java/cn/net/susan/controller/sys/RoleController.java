package cn.net.susan.controller.sys;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.NoLogin;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.sys.RoleConditionEntity;
import cn.net.susan.entity.sys.RoleEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.sys.RoleService;
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


@Api(tags = "角色操作", description = "角色接口")
@RestController
@RequestMapping("/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 通过id查询角色信息
     *
     * @param id 系统ID
     * @return 角色信息
     */
    @ApiOperation(notes = "通过id查询角色信息", value = "通过id查询角色信息")
    @GetMapping("/findById")
    public RoleEntity findById(Long id) {
        return roleService.findById(id);
    }

    /**
     * 根据条件查询角色列表
     *
     * @param roleConditionEntity 条件
     * @return 角色列表
     */
    @ApiOperation(notes = "根据条件查询角色列表", value = "根据条件查询角色列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<RoleEntity> searchByPage(@RequestBody RoleConditionEntity roleConditionEntity) {
        return roleService.searchByPage(roleConditionEntity);
    }


    /**
     * 根据查询所有角色
     *
     * @return 角色列表
     */
	@NoLogin
    @ApiOperation(notes = "根据查询所有角色", value = "根据查询所有角色")
    @GetMapping("/all")
    public List<RoleEntity> all() {
        return roleService.all();
    }

    /**
     * 添加角色
     *
     * @param roleEntity 角色实体
     * @return 影响行数
     */
    @ApiOperation(notes = "添加角色", value = "添加角色")
    @PostMapping("/insert")
    public int insert(@RequestBody RoleEntity roleEntity) {
        return roleService.insert(roleEntity);
    }

    /**
     * 修改角色
     *
     * @param roleEntity 角色实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改角色", value = "修改角色")
    @PostMapping("/update")
    public int update(@RequestBody RoleEntity roleEntity) {
        return roleService.update(roleEntity);
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色ID
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除角色", value = "批量删除角色")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return roleService.deleteByIds(ids);
    }


    /**
     * 导出角色数据
     *
     * @return 影响行数
     */
    @ExcelExport(ExcelBizTypeEnum.ROLE)
    @ApiOperation(notes = "导出角色数据", value = "导出角色数据")
    @PostMapping("/export")
    public void export(@RequestBody RoleConditionEntity roleConditionEntity) {
    }
}
