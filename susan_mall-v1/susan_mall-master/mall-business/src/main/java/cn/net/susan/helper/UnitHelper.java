package cn.net.susan.helper;

import cn.net.susan.entity.mall.BaseProductEntity;
import cn.net.susan.entity.mall.ProductGroupEntity;
import cn.net.susan.entity.mall.UnitConditionEntity;
import cn.net.susan.entity.mall.UnitEntity;
import cn.net.susan.mapper.mall.UnitMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class UnitHelper {

    @Autowired
    private UnitMapper unitMapper;


    /**
     * 添加单位信息
     *
     * @param list 商品信息
     */
    public void fillUnit(List<? extends BaseProductEntity> list) {
        List<Long> unitSysNoList = list.stream().map(BaseProductEntity::getUnitId).distinct().collect(Collectors.toList());
        UnitConditionEntity unitConditionEntity = new UnitConditionEntity();
        unitConditionEntity.setIdList(unitSysNoList);
        List<UnitEntity> unitEntities = unitMapper.searchByCondition(unitConditionEntity);
        if (CollectionUtils.isEmpty(unitEntities)) {
            return;
        }

        Map<Long, List<UnitEntity>> unitMap = unitEntities.stream().collect(Collectors.groupingBy(UnitEntity::getId));
        for (BaseProductEntity baseProductEntity : list) {
            baseProductEntity.setUnitName(unitMap.get(baseProductEntity.getUnitId()).get(0).getName());
        }
    }
}
