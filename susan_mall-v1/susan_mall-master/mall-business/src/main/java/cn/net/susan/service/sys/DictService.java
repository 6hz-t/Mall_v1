package cn.net.susan.service.sys;

import cn.hutool.json.JSONUtil;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.sys.DictConditionEntity;
import cn.net.susan.entity.sys.DictDetailConditionEntity;
import cn.net.susan.entity.sys.DictDetailEntity;
import cn.net.susan.entity.sys.DictEntity;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.sys.DictDetailMapper;
import cn.net.susan.mapper.sys.DictMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class DictService extends BaseService<DictEntity, DictConditionEntity> {

    private static final String DICT_DATA_KEY = "dictData";

    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private DictDetailMapper dictDetailMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询数据字典信息
     *
     * @param id 数据字典ID
     * @return 数据字典信息
     */
    public DictEntity findById(Long id) {
        return dictMapper.findById(id);
    }

    /**
     * 根据条件分页查询数据字典列表
     *
     * @param dictConditionEntity 数据字典信息
     * @return 数据字典集合
     */
    public ResponsePageEntity<DictEntity> searchByPage(DictConditionEntity dictConditionEntity) {
        return super.searchByPage(dictConditionEntity);
    }

    /**
     * 从缓存中获取数据字典
     *
     * @param dictName 数据字典名称
     * @return 数据字典
     */
    @Cacheable(value = "dict_data", keyGenerator = "dictCacheKeyGenerator")
    public List<DictDetailEntity> queryDictDetailEntity(String dictName) {
        List<DictDetailEntity> dataList = getDictDataFromRedis(dictName);
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        return dataList.stream().sorted((a, b) -> a.getSort().compareTo(b.getSort())).collect(Collectors.toList());
    }

    /**
     * 新增数据字典
     *
     * @param dictEntity 数据字典信息
     * @return 结果
     */
    public int insert(DictEntity dictEntity) {
        FillUserUtil.fillCreateUserInfo(dictEntity);
        return dictMapper.insert(dictEntity);
    }

    /**
     * 修改数据字典
     *
     * @param dictEntity 数据字典信息
     * @return 结果
     */
    public int update(DictEntity dictEntity) {
        FillUserUtil.fillUpdateUserInfo(dictEntity);
        return dictMapper.update(dictEntity);
    }

    /**
     * 删除数据字典对象
     *
     * @param ids 系统ID
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<DictEntity> dictEntities = dictMapper.findByIds(ids);
        AssertUtil.notEmpty(dictEntities, "数据字典详情已被删除");

        DictEntity dictEntity = new DictEntity();
        FillUserUtil.fillUpdateUserInfo(dictEntity);
        return dictMapper.deleteByIds(ids, dictEntity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return dictMapper;
    }

    /**
     * 更新redis中的数据字典
     */
    public void refreshDict() {
        DictConditionEntity dictConditionEntity = new DictConditionEntity();
        dictConditionEntity.setPageSize(0);
        List<DictEntity> dictEntities = dictMapper.searchByCondition(dictConditionEntity);
        if (CollectionUtils.isEmpty(dictEntities)) {
            return;
        }

        List<Long> dictIdList = dictEntities.stream().map(DictEntity::getId).collect(Collectors.toList());
        DictDetailConditionEntity dictDetailConditionEntity = new DictDetailConditionEntity();
        dictDetailConditionEntity.setDictIdList(dictIdList);
        dictDetailConditionEntity.setPageSize(0);
        List<DictDetailEntity> dictDetailEntities = dictDetailMapper.searchByCondition(dictDetailConditionEntity);
        Map<Long, List<DictDetailEntity>> dictDetailMap = dictDetailEntities.stream()
                .collect(Collectors.groupingBy(DictDetailEntity::getDictId));

        Map<Object, Object> dictMap = new HashMap<>(dictEntities.size());
        for (DictEntity dictEntity : dictEntities) {
            List<DictDetailEntity> detailEntityList = dictDetailMap.get(dictEntity.getId());
            dictMap.put(dictEntity.getDictName(), JSONUtil.toJsonStr(detailEntityList));
        }

        redisUtil.putHashMap(DICT_DATA_KEY, dictMap);
    }

    /**
     * 从redis中获取数据字典数据
     *
     * @return 数据字典数据
     */
    public List<DictDetailEntity> getDictDataFromRedis(String hashKey) {
        String json = (String) redisUtil.getHashValue(DICT_DATA_KEY, hashKey);
        if (!StringUtils.hasLength(json)) {
            return Collections.emptyList();
        }

        return JSONUtil.toList(json, DictDetailEntity.class);
    }
}
