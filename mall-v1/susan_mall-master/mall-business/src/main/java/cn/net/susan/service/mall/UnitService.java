package cn.net.susan.service.mall;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cn.net.susan.exception.BusinessException;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.BetweenTimeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.mall.UnitMapper;
import cn.net.susan.entity.mall.UnitConditionEntity;
import cn.net.susan.entity.mall.UnitEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;


@Service
public class UnitService extends BaseService<UnitEntity, UnitConditionEntity> {

    @Autowired
    private UnitMapper unitMapper;

    /**
     * 查询单位信息
     *
     * @param id 单位ID
     * @return 单位信息
     */
    public UnitEntity findById(Long id) {
        return unitMapper.findById(id);
    }

    /**
     * 根据条件分页查询单位列表
     *
     * @param unitConditionEntity 单位信息
     * @return 单位集合
     */
    public ResponsePageEntity<UnitEntity> searchByPage(UnitConditionEntity unitConditionEntity) {
        return super.searchByPage(unitConditionEntity);
    }

    /**
     * 新增单位
     *
     * @param unitEntity 单位信息
     * @return 结果
     */
    public int insert(UnitEntity unitEntity) {
        checkParam(unitEntity);
        return unitMapper.insert(unitEntity);
    }

    /**
     * 修改单位
     *
     * @param unitEntity 单位信息
     * @return 结果
     */
    public int update(UnitEntity unitEntity) {
        AssertUtil.notNull(unitEntity.getId(), "id不能为空");
        checkParam(unitEntity);
        return unitMapper.update(unitEntity);
    }

    private void checkParam(UnitEntity unitEntity) {
        UnitConditionEntity unitConditionEntity = new UnitConditionEntity();
        unitConditionEntity.setName(unitEntity.getName());
        List<UnitEntity> unitEntities = unitMapper.searchByCondition(unitConditionEntity);
        if (Objects.nonNull(unitEntity.getId())) {
            Optional<UnitEntity> optional = unitEntities.stream().filter(x -> !x.getId().equals(unitEntity.getId())).findAny();
            if (optional.isPresent()) {
                throw new BusinessException("该单位已存在");
            }
        } else {
            if (CollectionUtils.isNotEmpty(unitEntities)) {
                throw new BusinessException("该单位已存在");
            }
        }
    }

    /**
     * 批量删除单位对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<UnitEntity> entities = unitMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "单位已被删除");

        UnitEntity entity = new UnitEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return unitMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return unitMapper;
    }

}
