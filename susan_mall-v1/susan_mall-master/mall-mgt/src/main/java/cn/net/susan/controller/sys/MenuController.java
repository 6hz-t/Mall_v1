package cn.net.susan.controller.sys;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.NoLogin;
import cn.net.susan.dto.sys.MenuTreeDTO;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.sys.MenuConditionEntity;
import cn.net.susan.entity.sys.MenuEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.service.sys.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = "菜单操作", description = "菜单接口")
@RestController
@RequestMapping("/v1/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 通过id查询菜单信息
     *
     * @param id 系统ID
     * @return 菜单信息
     */
    @ApiOperation(notes = "通过id查询菜单信息", value = "通过id查询菜单信息")
    @GetMapping("/findById")
    public MenuEntity findById(Long id) {
        return menuService.findById(id);
    }

    /**
     * 根据条件查询菜单列表
     *
     * @param menuConditionEntity 条件
     * @return 菜单列表
     */
    @NoLogin
    @ApiOperation(notes = "根据条件查询菜单列表", value = "根据条件查询菜单列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<MenuEntity> searchByPage(@RequestBody MenuConditionEntity menuConditionEntity) {
        return menuService.searchByPage(menuConditionEntity);
    }

    /**
     * 获取菜单树
     *
     * @return 菜单列表
     */
    @ApiOperation(notes = "获取菜单树", value = "获取菜单树")
    @GetMapping("/getMenuTree")
    public List<MenuTreeDTO> getMenuTree() {
        return menuService.getMenuTree();
    }


    /**
     * 获取下级菜单
     *
     * @return 菜单列表
     */
    @ApiOperation(notes = "获取下级菜单", value = "获取下级菜单")
    @GetMapping("/getChild")
    public List<Long> getChild(@RequestParam("id") Long id) {
        return menuService.getChild(id);
    }

    /**
     * 获取逐级加载的菜单
     *
     * @return 菜单列表
     */
    @ApiOperation(notes = "获取逐级加载的菜单", value = "获取逐级加载的菜单")
    @PostMapping("/getMenu")
    public List<MenuTreeDTO> getMenu(@RequestBody MenuConditionEntity menuConditionEntity) {
        return menuService.getMenu(menuConditionEntity);
    }

    /**
     * 添加菜单
     *
     * @param menuEntity 菜单实体
     * @return 影响行数
     */
    @NoLogin
    @ApiOperation(notes = "添加菜单", value = "添加菜单")
    @PostMapping("/insert")
    public int insert(@RequestBody MenuEntity menuEntity) {
        return menuService.insert(menuEntity);
    }

    /**
     * 修改菜单
     *
     * @param menuEntity 菜单实体
     * @return 影响行数
     */
    @NoLogin
    @ApiOperation(notes = "修改菜单", value = "修改菜单")
    @PostMapping("/update")
    public int update(@RequestBody MenuEntity menuEntity) {
        return menuService.update(menuEntity);
    }

    /**
     * 批量删除菜单
     *
     * @param ids 菜单ID
     * @return 影响行数
     */
    @NoLogin
    @ApiOperation(notes = "删除菜单", value = "删除菜单")
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return menuService.deleteByIds(ids);
    }


    /**
     * 导出菜单数据
     *
     * @return 影响行数
     */
    @ExcelExport(ExcelBizTypeEnum.MENU)
    @NoLogin
    @ApiOperation(notes = "导出菜单数据", value = "导出菜单数据")
    @PostMapping("/export")
    public void export(@RequestBody MenuConditionEntity menuConditionEntity) {
    }
}
