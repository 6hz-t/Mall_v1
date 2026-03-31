package cn.net.susan.service.mall;

import java.util.List;

import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.mall.ProductGroupAttributeMapper;
import cn.net.susan.entity.mall.ProductGroupAttributeConditionEntity;
import cn.net.susan.entity.mall.ProductGroupAttributeEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;

 
@Service
public class ProductGroupAttributeService extends BaseService<ProductGroupAttributeEntity, ProductGroupAttributeConditionEntity> {

	@Autowired
	private ProductGroupAttributeMapper productGroupAttributeMapper;

	/**
     * 查询商品组属性信息
     *
     * @param id 商品组属性ID
     * @return 商品组属性信息
     */
	public ProductGroupAttributeEntity findById(Long id) {
	    return productGroupAttributeMapper.findById(id);
	}

	/**
     * 根据条件分页查询商品组属性列表
     *
     * @param productGroupAttributeConditionEntity 商品组属性信息
     * @return 商品组属性集合
     */
	public ResponsePageEntity<ProductGroupAttributeEntity> searchByPage(ProductGroupAttributeConditionEntity productGroupAttributeConditionEntity) {
		return super.searchByPage(productGroupAttributeConditionEntity);
	}

    /**
     * 新增商品组属性
     *
     * @param productGroupAttributeEntity 商品组属性信息
     * @return 结果
     */
	public int insert(ProductGroupAttributeEntity productGroupAttributeEntity) {
	    return productGroupAttributeMapper.insert(productGroupAttributeEntity);
	}

	/**
     * 修改商品组属性
     *
     * @param productGroupAttributeEntity 商品组属性信息
     * @return 结果
     */
	public int update(ProductGroupAttributeEntity productGroupAttributeEntity) {
	    return productGroupAttributeMapper.update(productGroupAttributeEntity);
	}

	/**
     * 批量删除商品组属性
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<ProductGroupAttributeEntity> entities = productGroupAttributeMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "商品组属性已被删除");

		ProductGroupAttributeEntity entity = new ProductGroupAttributeEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return productGroupAttributeMapper.deleteByIds(ids, entity);
	}

    @Override
    protected BaseMapper getBaseMapper() {
        return productGroupAttributeMapper;
    }
}
