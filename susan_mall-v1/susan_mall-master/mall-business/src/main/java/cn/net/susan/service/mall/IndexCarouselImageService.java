package cn.net.susan.service.mall;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.net.susan.service.BaseService;
import cn.net.susan.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.mall.IndexCarouselImageMapper;
import cn.net.susan.entity.mall.IndexCarouselImageConditionEntity;
import cn.net.susan.entity.mall.IndexCarouselImageEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Slf4j
@Service
public class IndexCarouselImageService extends BaseService<IndexCarouselImageEntity, IndexCarouselImageConditionEntity> {

    private static final String INDEX_CAROUSE_IMAGE_KEY = "indexCarouselImage";

    @Autowired
    private IndexCarouselImageMapper indexCarouselImageMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取首页轮播图列表
     *
     * @return 首页轮播图列表
     */
    public List<IndexCarouselImageEntity> getIndexCarouselImageList() {
        String value = redisUtil.get(INDEX_CAROUSE_IMAGE_KEY);
        return JSON.parseArray(value, IndexCarouselImageEntity.class);
    }

    /**
     * 刷新首页轮播图到Redis中
     *
     * @param top 轮播图数量
     */
    public void refreshIndexCarouseImageToRedis(int top, String sortParam) {
        IndexCarouselImageConditionEntity indexCarouselImageConditionEntity = new IndexCarouselImageConditionEntity();
        indexCarouselImageConditionEntity.setPageNo(1);
        indexCarouselImageConditionEntity.setPageSize(top);
        if (StringUtils.hasLength(sortParam)) {
            indexCarouselImageConditionEntity.setSortField(Arrays.stream(sortParam.split(" ")).collect(Collectors.toList()));
        }

        List<IndexCarouselImageEntity> indexCarouselImageEntities = indexCarouselImageMapper.searchByCondition(indexCarouselImageConditionEntity);
        if (CollectionUtils.isEmpty(indexCarouselImageEntities)) {
            return;
        }

        redisUtil.set(INDEX_CAROUSE_IMAGE_KEY, JSON.toJSONString(indexCarouselImageEntities));
        log.info("refreshToRedis 更新完成");
    }


    /**
     * 查询首页轮播图信息
     *
     * @param id 首页轮播图ID
     * @return 首页轮播图信息
     */
    public IndexCarouselImageEntity findById(Long id) {
        return indexCarouselImageMapper.findById(id);
    }

    /**
     * 根据条件分页查询首页轮播图列表
     *
     * @param indexCarouselImageConditionEntity 首页轮播图信息
     * @return 首页轮播图集合
     */
    public ResponsePageEntity<IndexCarouselImageEntity> searchByPage(IndexCarouselImageConditionEntity indexCarouselImageConditionEntity) {
        return super.searchByPage(indexCarouselImageConditionEntity);
    }

    /**
     * 新增首页轮播图
     *
     * @param indexCarouselImageEntity 首页轮播图信息
     * @return 结果
     */
    public int insert(IndexCarouselImageEntity indexCarouselImageEntity) {
        return indexCarouselImageMapper.insert(indexCarouselImageEntity);
    }

    /**
     * 修改首页轮播图
     *
     * @param indexCarouselImageEntity 首页轮播图信息
     * @return 结果
     */
    public int update(IndexCarouselImageEntity indexCarouselImageEntity) {
        return indexCarouselImageMapper.update(indexCarouselImageEntity);
    }

    /**
     * 批量删除首页轮播图对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<IndexCarouselImageEntity> entities = indexCarouselImageMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "首页轮播图已被删除");

        IndexCarouselImageEntity entity = new IndexCarouselImageEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return indexCarouselImageMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return indexCarouselImageMapper;
    }

}
