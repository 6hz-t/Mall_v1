package cn.net.susan.controller.shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.shopping.ProductFavoritesConditionEntity;
import cn.net.susan.entity.shopping.ProductFavoritesEntity;
import cn.net.susan.service.shopping.ProductFavoritesService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/v1/productFavorites")
public class ProductFavoritesController {

    @Autowired
    private ProductFavoritesService productFavoritesService;

    /**
     * 通过id查询商品收藏信息
     *
     * @param id 系统ID
     * @return 商品收藏信息
     */
    @GetMapping("/findById")
    public ProductFavoritesEntity findById(Long id) {
        return productFavoritesService.findById(id);
    }

    /**
     * 根据条件查询商品收藏列表
     *
     * @param productFavoritesConditionEntity 条件
     * @return 商品收藏列表
     */
    @PostMapping("/searchByPage")
    public ResponsePageEntity<ProductFavoritesEntity> searchByPage(@RequestBody ProductFavoritesConditionEntity productFavoritesConditionEntity) {
        return productFavoritesService.searchByPage(productFavoritesConditionEntity);
    }


    /**
     * 添加商品收藏
     *
     * @param productFavoritesEntity 商品收藏实体
     * @return 影响行数
     */
    @PostMapping("/insert")
    public void insert(@RequestBody @Valid ProductFavoritesEntity productFavoritesEntity) {
        productFavoritesService.insert(productFavoritesEntity);
    }

    /**
     * 修改商品收藏
     *
     * @param productFavoritesEntity 商品收藏实体
     * @return 影响行数
     */
    @PostMapping("/update")
    public int update(@RequestBody ProductFavoritesEntity productFavoritesEntity) {
        return productFavoritesService.update(productFavoritesEntity);
    }

    /**
     * 批量删除商品收藏
     *
     * @param ids 商品收藏ID集合
     * @return 影响行数
     */
    @PostMapping("/deleteByIds")
    public int deleteByIds(@RequestBody @NotNull List<Long> ids) {
        return productFavoritesService.deleteByIds(ids);
    }
}
