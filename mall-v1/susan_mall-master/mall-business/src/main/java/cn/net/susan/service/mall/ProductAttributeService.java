package cn.net.susan.service.mall;

import java.util.List;

import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.mall.ProductAttributeMapper;
import cn.net.susan.entity.mall.ProductAttributeConditionEntity;
import cn.net.susan.entity.mall.ProductAttributeEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;

@Service
public class ProductAttributeService extends BaseService< ProductAttributeEntity,  ProductAttributeConditionEntity> {

	@Autowired
	private ProductAttributeMapper productAttributeMapper;

	/**
     * 查询商品属性信息
     *
     * @param id 商品属性ID
     * @return 商品属性信息
     */
	public ProductAttributeEntity findById(Long id) {
	    return productAttributeMapper.findById(id);
	}

	/**
     * 根据条件分页查询商品属性列表
     *
     * @param productAttributeConditionEntity 商品属性信息
     * @return 商品属性集合
     */
	public ResponsePageEntity<ProductAttributeEntity> searchByPage(ProductAttributeConditionEntity productAttributeConditionEntity) {
		int count = productAttributeMapper.searchCount(productAttributeConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(productAttributeConditionEntity);
		}
		List<ProductAttributeEntity> dataList = productAttributeMapper.searchByCondition(productAttributeConditionEntity);
		return ResponsePageEntity.build(productAttributeConditionEntity, count, dataList);
	}

    /**
     * 新增商品属性
     *
     * @param productAttributeEntity 商品属性信息
     * @return 结果
     */
	public int insert(ProductAttributeEntity productAttributeEntity) {
	    return productAttributeMapper.insert(productAttributeEntity);
	}

	/**
     * 修改商品属性
     *
     * @param productAttributeEntity 商品属性信息
     * @return 结果
     */
	public int update(ProductAttributeEntity productAttributeEntity) {
	    return productAttributeMapper.update(productAttributeEntity);
	}

	/**
     * 批量删除商品属性对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<ProductAttributeEntity> entities = productAttributeMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "商品属性已被删除");

		ProductAttributeEntity entity = new ProductAttributeEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return productAttributeMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return productAttributeMapper;
	}

}
