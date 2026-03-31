package cn.net.susan.controller.web;

import cn.net.susan.annotation.NoLogin;
import cn.net.susan.annotation.RepeatSubmit;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.ProductConditionEntity;
import cn.net.susan.entity.mall.web.OrderTradeProductCommentWebEntity;
import cn.net.susan.entity.mall.web.ProductCommentWebEntity;
import cn.net.susan.entity.mall.web.ProductConditionWebEntity;
import cn.net.susan.entity.mall.web.ProductDetailWebEntity;
import cn.net.susan.entity.mall.web.ProductWebEntity;
import cn.net.susan.entity.shopping.ProductCommentConditionEntity;
import cn.net.susan.entity.shopping.ProductFavoritesEntity;
import cn.net.susan.entity.shopping.ProductViewRecordEntity;
import cn.net.susan.entity.shopping.ShoppingCartConditionEntity;
import cn.net.susan.entity.shopping.ShoppingCartEntity;
import cn.net.susan.entity.shopping.web.ShoppingCartProductWebEntity;
import cn.net.susan.entity.shopping.web.ShoppingCartWebEntity;
import cn.net.susan.service.mall.ProductService;
import cn.net.susan.service.shopping.ProductCommentService;
import cn.net.susan.service.shopping.ProductFavoritesService;
import cn.net.susan.service.shopping.ProductViewRecordService;
import cn.net.susan.service.shopping.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = "web商品操作", description = "web商品操作")
@RestController
@RequestMapping("/v1/web/product")
@Validated
public class WebProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductFavoritesService productFavoritesService;
    @Autowired
    private ProductViewRecordService productViewRecordService;
    @Autowired
    private ProductCommentService productCommentService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 新增或取消收藏商品
     *
     * @param productFavoritesEntity 商品收藏实体
     */
    @PostMapping("/addOrCancelFavorites")
    public Boolean addOrCancelFavorites(@RequestBody @Valid ProductFavoritesEntity productFavoritesEntity) {
        return productFavoritesService.addOrCancelFavorites(productFavoritesEntity);
    }

    /**
     * 新增商品浏览记录
     *
     * @param productViewRecordEntity 商品实体
     */
    @PostMapping("/addViewRecord")
    public void addViewRecord(@RequestBody @Valid ProductViewRecordEntity productViewRecordEntity) {
        productViewRecordService.insert(productViewRecordEntity);
    }

    /**
     * 获取商品详情
     *
     * @param productId 商品ID
     * @return 商品详情
     */
    @NoLogin
    @ApiOperation(notes = "获取商品详情", value = "获取商品详情")
    @GetMapping("/getDetail")
    public ProductDetailWebEntity getDetail(@RequestParam("productId") Long productId) {
        return productService.getDetail(productId);
    }

    /**
     * 根据条件搜索商品列表
     *
     * @param productConditionEntity 条件
     * @return 商品列表
     */
    @NoLogin
    @ApiOperation(notes = "根据条件搜索商品列表", value = "根据条件搜索商品列表")
    @PostMapping("/search")
    public ResponsePageEntity<ProductWebEntity> search(@RequestBody ProductConditionWebEntity productConditionEntity) {
        return productService.searchFromES(productConditionEntity);
    }

    /**
     * 根据条件搜索商品评论列表
     *
     * @param productCommentConditionEntity 条件
     * @return 商品评论列表
     */
    @NoLogin
    @ApiOperation(notes = "根据条件搜索商品评论列表", value = "根据条件搜索商品评论列表")
    @PostMapping("/searchProductComment")
    public ResponsePageEntity<ProductCommentWebEntity> searchProductComment(@RequestBody ProductCommentConditionEntity productCommentConditionEntity) {
        return productCommentService.searchProductComment(productCommentConditionEntity);
    }

    /**
     * 根据条件搜索购物车商品列表
     *
     * @param shoppingCartConditionEntity 条件
     * @return 购物车商品列表
     */
    @ApiOperation(notes = "根据条件搜索购物车商品列表", value = "根据条件搜索购物车商品列表")
    @PostMapping("/getShoppingCartProduct")
    public ShoppingCartWebEntity getShoppingCartProduct(@RequestBody ShoppingCartConditionEntity shoppingCartConditionEntity) {
        return shoppingCartService.getShoppingCartProduct(shoppingCartConditionEntity);
    }

    /**
     * 添加购物车
     *
     * @param shoppingCartEntity 购物车信息
     */
    @ApiOperation(notes = "添加购物车", value = "添加购物车")
    @PostMapping("/addShoppingCart")
    public Boolean addShoppingCart(@RequestBody ShoppingCartEntity shoppingCartEntity) {
        return shoppingCartService.addShoppingCart(shoppingCartEntity);
    }

    /**
     * 修改购物车
     *
     * @param shoppingCartEntity 购物车信息
     */
    @ApiOperation(notes = "修改购物车", value = "修改购物车")
    @PostMapping("/updateShoppingCart")
    public void updateShoppingCart(@RequestBody ShoppingCartEntity shoppingCartEntity) {
        shoppingCartService.updateShoppingCart(shoppingCartEntity);
    }

    /**
     * 批量删除购物车
     *
     * @param ids 购物车ID集合
     * @return 影响行数
     */
    @ApiOperation(notes = "批量删除购物车", value = "批量删除购物车")
    @PostMapping("/deleteShoppingCart")
    public int deleteShoppingCart(@RequestBody @NotNull List<Long> ids) {
        return shoppingCartService.deleteByIds(ids);
    }

    /**
     * 批量保存的商品用户评价
     *
     * @param orderTradeProductCommentWebEntity 订单商品评价实体
     * @return 影响行数
     */
    @RepeatSubmit
    @ApiOperation(notes = "批量保存的商品用户评价", value = "批量保存的商品用户评价")
    @PostMapping("/saveProductComment")
    public void saveProductComment(@RequestBody  @NotNull OrderTradeProductCommentWebEntity orderTradeProductCommentWebEntity) {
        productCommentService.saveProductComment(orderTradeProductCommentWebEntity);
    }
}
