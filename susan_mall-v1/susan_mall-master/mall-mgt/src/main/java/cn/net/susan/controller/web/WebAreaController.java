package cn.net.susan.controller.web;

import cn.net.susan.entity.common.web.CommonAreaWebEntity;
import cn.net.susan.service.common.CommonAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = "web地区操作", description = "web地区操作")
@RestController
@RequestMapping("/v1/web/area")
@Validated
public class WebAreaController {

    @Autowired
    private CommonAreaService commonAreaService;

    /**
     * 根据parentId获取地区列表
     *
     * @param parentId 上级地区ID
     * @return 地区列表
     */
    @ApiOperation(notes = "根据parentId获取地区列表", value = "根据parentId获取地区列表")
    @GetMapping("/getAreaByParentId")
    public List<CommonAreaWebEntity> getAreaByParentId(@RequestParam("parentId") Long parentId) {
        return commonAreaService.getAreaByParentId(parentId);
    }

}
