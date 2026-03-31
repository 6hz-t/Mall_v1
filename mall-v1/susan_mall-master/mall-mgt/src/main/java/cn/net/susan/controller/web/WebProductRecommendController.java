package cn.net.susan.controller.web;

import cn.net.susan.entity.mall.web.ProductWebEntity;
import cn.net.susan.service.recommend.RecommendProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = "web推荐操作", description = "web推荐操作")
@RestController
@RequestMapping("/v1/web/recommend")
@Validated
public class WebProductRecommendController {

    @Autowired
    private RecommendProductService recommendProductService;

    /**
     * 获取当前登录用户喜好的商品列表
     *
     * @return 商品列表
     */
    @ApiOperation(notes = "获取当前登录用户喜好的商品列表", value = "获取当前登录用户喜好的商品列表")
    @GetMapping("/recommendProduct")
    public List<ProductWebEntity> recommendProduct() {
        return recommendProductService.recommendProduct();
    }
}
