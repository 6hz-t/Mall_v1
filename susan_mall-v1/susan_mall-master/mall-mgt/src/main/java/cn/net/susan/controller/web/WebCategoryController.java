package cn.net.susan.controller.web;

import cn.net.susan.annotation.NoLogin;
import cn.net.susan.entity.mall.web.CategoryWebEntity;
import cn.net.susan.service.mall.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = "web分类操作", description = "web分类操作")
@RestController
@RequestMapping("/v1/web/category")
@Validated
public class WebCategoryController {


    @Autowired
    private CategoryService categoryService;


    /**
     * 根据父分类ID查询分类列表
     *
     * @param parentId 父分类ID
     * @return 分类列表
     */
    @NoLogin
    @ApiOperation(notes = "根据父分类ID查询分类列表", value = "根据父分类ID查询分类列表")
    @GetMapping("/getCategoryByParentId")
    public List<CategoryWebEntity> getCategoryByParentId(@RequestParam("parentId") Long parentId) {
        return categoryService.getCategoryByParentId(parentId);
    }
}
