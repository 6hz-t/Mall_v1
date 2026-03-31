package cn.net.susan.service.sys;

import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.sys.DictConditionEntity;
import cn.net.susan.entity.sys.DictDetailConditionEntity;
import cn.net.susan.entity.sys.DictDetailEntity;
import cn.net.susan.entity.sys.DictEntity;
import cn.net.susan.mapper.sys.DictDetailMapper;
import cn.net.susan.mapper.sys.DictMapper;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class DictDetailService {

    @Autowired
    private DictDetailMapper dictDetailMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private DictMapper dictMapper;

    /**
     * 查询信息
     *
     * @param id 数据字典ID
     * @return 数据字典信息
     */
    public DictDetailEntity findById(Long id) {
        return dictDetailMapper.findById(id);
    }

    /**
     * 根据条件分页查询数据字典列表
     *
     * @param dictDetailConditionEntity 数据字典信息
     * @return 数据字典集合
     */
    public ResponsePageEntity<DictDetailEntity> searchByPage(DictDetailConditionEntity dictDetailConditionEntity) {
        if (StringUtils.hasLength(dictDetailConditionEntity.getDictName())) {
            DictConditionEntity dictConditionEntity = new DictConditionEntity();
            dictConditionEntity.setDictName(dictDetailConditionEntity.getDictName());
            List<DictEntity> dictEntities = dictMapper.searchByCondition(dictConditionEntity);
            if (!CollectionUtils.isEmpty(dictEntities)) {
                dictDetailConditionEntity.setDictId(dictEntities.get(0).getId());
            }
        }

        int count = dictDetailMapper.searchCount(dictDetailConditionEntity);
        if (count == 0) {
            return ResponsePageEntity.buildEmpty(dictDetailConditionEntity);
        }
        List<DictDetailEntity> dataList = dictDetailMapper.searchByCondition(dictDetailConditionEntity);
        return ResponsePageEntity.build(dictDetailConditionEntity, count, dataList);
    }

    /**
     * 从缓存中查询数据字典详情
     *
     * @param dictDetailConditionEntity 查询条件
     * @return 数据字典详情
     */
    public List<DictDetailEntity> searchDictDetailFromCache(DictDetailConditionEntity dictDetailConditionEntity) {
        return dictService.queryDictDetailEntity(dictDetailConditionEntity.getDictName());
    }

    /**
     * 新增数据字典
     *
     * @param dictDetailEntity 数据字典信息
     * @return 结果
     */
    public int insert(DictDetailEntity dictDetailEntity) {
        AssertUtil.notNull(dictDetailEntity.getDict(), "dict不能为空");
        FillUserUtil.fillCreateUserInfo(dictDetailEntity);
        dictDetailEntity.setDictId(dictDetailEntity.getDict().getId());
        return dictDetailMapper.insert(dictDetailEntity);
    }

    /**
     * 修改数据字典
     *
     * @param dictDetailEntity 数据字典信息
     * @return 结果
     */
    public int update(DictDetailEntity dictDetailEntity) {
        FillUserUtil.fillUpdateUserInfo(dictDetailEntity);
        return dictDetailMapper.update(dictDetailEntity);
    }

    /**
     * 删除数据字典门对象
     *
     * @param ids 系统ID
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<DictDetailEntity> detailEntityList = dictDetailMapper.findByIds(ids);
        AssertUtil.notEmpty(detailEntityList, "数据字典详情已被删除");

        DictDetailEntity dictDetailEntity = new DictDetailEntity();
        FillUserUtil.fillUpdateUserInfo(dictDetailEntity);
        return dictDetailMapper.deleteByIds(ids, dictDetailEntity);
    }


}
