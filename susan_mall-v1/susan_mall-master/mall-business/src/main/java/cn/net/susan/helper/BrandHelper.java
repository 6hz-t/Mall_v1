package cn.net.susan.helper;

import cn.net.susan.entity.mall.BrandConditionEntity;
import cn.net.susan.entity.mall.BrandEntity;
import cn.net.susan.entity.mall.ProductEntity;
import cn.net.susan.mapper.mall.BrandMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class BrandHelper {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 添加品牌信息
     *
     * @param list 商品信息
     */
    public void fillBrand(List<ProductEntity> list) {
        List<Long> brandSysNoList = list.stream().map(ProductEntity::getBrandId).distinct().collect(Collectors.toList());
        BrandConditionEntity brandConditionEntity = new BrandConditionEntity();
        brandConditionEntity.setIdList(brandSysNoList);
        List<BrandEntity> brandEntities = brandMapper.searchByCondition(brandConditionEntity);
        if (CollectionUtils.isEmpty(brandEntities)) {
            return;
        }

        Map<Long, List<BrandEntity>> brandMap = brandEntities.stream().collect(Collectors.groupingBy(BrandEntity::getId));
        for (ProductEntity productEntity : list) {
            List<BrandEntity> findBrandList = brandMap.get(productEntity.getBrandId());
            if (CollectionUtils.isEmpty(findBrandList)) {
                continue;
            }
            productEntity.setBrandName(findBrandList.get(0).getName());
        }
    }
}
