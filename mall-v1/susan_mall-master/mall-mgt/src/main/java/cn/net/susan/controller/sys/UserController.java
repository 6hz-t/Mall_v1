package cn.net.susan.controller.sys;

import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.annotation.NoLogin;
import cn.net.susan.entity.sys.DeptConditionEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.sys.UserConditionEntity;
import cn.net.susan.entity.sys.UserEntity;
import cn.net.susan.service.sys.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.List;


@Slf4j
@Api(tags = "用户操作", description = "用户接口")
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 通过id查询用户信息
     *
     * @param id 系统ID
     * @return 用户信息
     */
    @PreAuthorize("@el.checkPermission('admin','user:list')")
    @ApiOperation(notes = "通过id查询用户信息", value = "通过id查询用户信息")
    @GetMapping("/findById")
    public UserEntity findById(Long id) {
        UserEntity userEntity = userService.findById(id);
        log.info("userEntity:{}", userEntity);
        return userEntity;
    }

    /**
     * 根据条件查询用户列表
     *
     * @param userConditionEntity 条件
     * @return 用户列表
     */
    @ApiOperation(notes = "根据条件查询用户列表", value = "根据条件查询用户列表")
    @PostMapping("/searchByPage")
    public ResponsePageEntity<UserEntity> searchByPage(@RequestBody UserConditionEntity userConditionEntity) {
        return userService.searchByPage(userConditionEntity);
    }


    /**
     * 添加用户
     *
     * @param userEntity 用户实体
     * @return 影响行数
     */
    @NoLogin
    @ApiOperation(notes = "添加用户", value = "添加用户")
    @PostMapping("/insert")
    public void insert(@RequestBody UserEntity userEntity) {
        userService.insert(userEntity);
    }

    /**
     * 修改用户
     *
     * @param userEntity 用户实体
     * @return 影响行数
     */
    @ApiOperation(notes = "修改用户", value = "修改用户")
    @PostMapping("/update")
    public int update(@RequestBody UserEntity userEntity) {
        return userService.update(userEntity);
    }

    /**
     * 删除用户
     *
     * @param ids 用户ID
     * @return 影响行数
     */
    @ApiOperation(notes = "删除用户", value = "删除用户")
    @PostMapping("/deleteByIds")
    public int deleteById(@RequestBody@NotNull List<Long> ids) {
        return userService.deleteByIds(ids);
    }


    /**
     * 重置密码
     *
     * @param ids 用户ID
     * @return 影响行数
     */
    @ApiOperation(notes = "重置密码", value = "重置密码")
    @PostMapping("/resetPwd")
    public int resetPwd(@RequestBody@NotNull List<Long> ids) {
        return userService.resetPwd(ids);
    }

    /**
     * 导出用户数据
     *
     * @return 影响行数
     */
    @ExcelExport(ExcelBizTypeEnum.USER)
    @NoLogin
    @ApiOperation(notes = "导出用户数据", value = "导出用户数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserConditionEntity userConditionEntity) throws IOException {
    }
}
