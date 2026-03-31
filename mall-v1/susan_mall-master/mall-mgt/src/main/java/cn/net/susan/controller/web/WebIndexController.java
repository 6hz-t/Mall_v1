package cn.net.susan.controller.web;

import cn.net.susan.annotation.NoLogin;
import cn.net.susan.entity.RequestPageEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.mall.IndexCarouselImageEntity;
import cn.net.susan.entity.mall.IndexProductEntity;
import cn.net.susan.entity.mall.web.IndexNoticeDetailWebEntity;
import cn.net.susan.entity.mall.web.IndexNoticeWebEntity;
import cn.net.susan.entity.mall.web.ProductConditionWebEntity;
import cn.net.susan.entity.mall.web.ProductWebEntity;
import cn.net.susan.service.mall.IndexCarouselImageService;
import cn.net.susan.service.mall.IndexNoticeService;
import cn.net.susan.service.mall.IndexProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = "web首页操作", description = "web首页操作")
@RestController
@RequestMapping("/v1/web/index")
public class WebIndexController {

    @Autowired
    private IndexCarouselImageService indexCarouselImageService;
    @Autowired
    private IndexProductService indexProductService;
    @Autowired
    private IndexNoticeService indexNoticeService;

    /**
     * 获取首页轮播图列表
     *
     * @return 首页轮播图列表
     */
    @NoLogin
    @GetMapping("/getIndexCarouselImageList")
    public List<IndexCarouselImageEntity> getIndexCarouselImageList() {
        return indexCarouselImageService.getIndexCarouselImageList();
    }

    /**
     * 获取首页商品列表
     *
     * @return 首页商品列表
     */
    @NoLogin
    @GetMapping("/getIndexProductList")
    public List<IndexProductEntity> getIndexProductList(@RequestParam("type") int type) {
        return indexProductService.getIndexProductList(type);
    }

    /**
     * 获取首页公告列表
     *
     * @return 公告列表
     */
    @NoLogin
    @ApiOperation(notes = "获取首页公告列表", value = "获取首页公告列表")
    @GetMapping("/getIndexNoticeList")
    public List<IndexNoticeWebEntity> getIndexNoticeList() {
        return indexNoticeService.getIndexNoticeList();
    }

    /**
     * 根据条件搜索公告列表
     *
     * @param requestPageEntity 条件
     * @return 公告列表
     */
    @NoLogin
    @ApiOperation(notes = "根据条件搜索公告列表", value = "根据条件搜索公告列表")
    @PostMapping("/searchIndexNoticeByPage")
    public ResponsePageEntity<IndexNoticeWebEntity> searchIndexNoticeByPage(@RequestBody RequestPageEntity requestPageEntity) {
        return indexNoticeService.searchIndexNoticeByPage(requestPageEntity);
    }

    /**
     * 查询公告详情
     *
     * @param id 公告系统ID
     * @return 公告详情
     */
    @NoLogin
    @ApiOperation(notes = "查询公告详情", value = "查询公告详情")
    @GetMapping("/getIndexNoticeDetail")
    public IndexNoticeDetailWebEntity getIndexNoticeDetail(@RequestParam("id") Long id) {
        return indexNoticeService.getIndexNoticeDetail(id);
    }
}
