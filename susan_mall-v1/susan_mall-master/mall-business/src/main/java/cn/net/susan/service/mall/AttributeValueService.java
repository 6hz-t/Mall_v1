package cn.net.susan.service.mall;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.net.susan.entity.mall.AttributeConditionEntity;
import cn.net.susan.entity.mall.AttributeEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.entity.mall.UnitConditionEntity;
import cn.net.susan.entity.mall.UnitEntity;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.mapper.mall.AttributeMapper;
import cn.net.susan.service.BaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.mall.AttributeValueMapper;
import cn.net.susan.entity.mall.AttributeValueConditionEntity;
import cn.net.susan.entity.mall.AttributeValueEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;


@Service
public class AttributeValueService extends BaseService<AttributeValueEntity, AttributeValueConditionEntity> {

    @Autowired
    private AttributeValueMapper attributeValueMapper;
    @Autowired
    private AttributeMapper attributeMapper;

    /**
     * 查询属性值信息
     *
     * @param id 属性值ID
     * @return 属性值信息
     */
    public AttributeValueEntity findById(Long id) {
        return attributeValueMapper.findById(id);
    }

    /**
     * 根据条件分页查询属性值列表
     *
     * @param attributeValueConditionEntity 属性值信息
     * @return 属性值集合
     */
    public ResponsePageEntity<AttributeValueEntity> searchByPage(AttributeValueConditionEntity attributeValueConditionEntity) {
        ResponsePageEntity<AttributeValueEntity> attributeValueEntityResponsePageEntity = super.searchByPage(attributeValueConditionEntity);
        if (CollectionUtils.isNotEmpty(attributeValueEntityResponsePageEntity.getData())) {
            fillAttribute(attributeValueEntityResponsePageEntity.getData());
        }
        return attributeValueEntityResponsePageEntity;
    }


    private void fillAttribute(List<AttributeValueEntity> list) {
        List<Long> attributeSysNoList = list.stream().map(AttributeValueEntity::getAttributeId).distinct().collect(Collectors.toList());
        AttributeConditionEntity attributeConditionEntity = new AttributeConditionEntity();
        attributeConditionEntity.setIdList(attributeSysNoList);
        List<AttributeEntity> attributeEntities = attributeMapper.searchByCondition(attributeConditionEntity);
        if (CollectionUtils.isEmpty(attributeEntities)) {
            return;
        }

        Map<Long, List<AttributeEntity>> attributeMap = attributeEntities.stream().collect(Collectors.groupingBy(AttributeEntity::getId));
        for (AttributeValueEntity attributeValueEntity : list) {
            attributeValueEntity.setAttributeName(attributeMap.get(attributeValueEntity.getAttributeId()).get(0).getName());
        }
    }

    /**
     * 新增属性值
     *
     * @param attributeValueEntity 属性值信息
     * @return 结果
     */
    public int insert(AttributeValueEntity attributeValueEntity) {
        checkParam(attributeValueEntity);
        return attributeValueMapper.insert(attributeValueEntity);
    }

    /**
     * 修改属性值
     *
     * @param attributeValueEntity 属性值信息
     * @return 结果
     */
    public int update(AttributeValueEntity attributeValueEntity) {
        AssertUtil.notNull(attributeValueEntity.getId(), "id不能为空");
        checkParam(attributeValueEntity);
        return attributeValueMapper.update(attributeValueEntity);
    }

    private void checkParam(AttributeValueEntity attributeValueEntity) {
		attributeValueEntity.setValue(attributeValueEntity.getValue().trim());

        AttributeValueConditionEntity attributeValueConditionEntity = new AttributeValueConditionEntity();
        attributeValueConditionEntity.setAttributeId(attributeValueEntity.getAttributeId());
        attributeValueConditionEntity.setValue(attributeValueEntity.getValue().trim());
        List<AttributeValueEntity> attributeValueEntities = attributeValueMapper.searchByCondition(attributeValueConditionEntity);
        if (Objects.nonNull(attributeValueEntity.getId())) {
            Optional<AttributeValueEntity> optional = attributeValueEntities.stream()
                    .filter(x -> !x.getId().equals(attributeValueEntity.getId())).findAny();
            if (optional.isPresent()) {
                throw new BusinessException("该属性值已存在");
            }
        } else {
            if (CollectionUtils.isNotEmpty(attributeValueEntities)) {
                throw new BusinessException("该属性值已存在");
            }
        }
    }

    /**
     * 批量删除属性值对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<AttributeValueEntity> entities = attributeValueMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "属性值已被删除");

        AttributeValueEntity entity = new AttributeValueEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return attributeValueMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return attributeValueMapper;
    }

}
