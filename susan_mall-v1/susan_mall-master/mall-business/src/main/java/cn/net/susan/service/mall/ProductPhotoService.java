package cn.net.susan.service.mall;

import java.util.List;

import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.mall.ProductPhotoMapper;
import cn.net.susan.entity.mall.ProductPhotoConditionEntity;
import cn.net.susan.entity.mall.ProductPhotoEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;

@Service
public class ProductPhotoService extends BaseService< ProductPhotoEntity,  ProductPhotoConditionEntity> {

	@Autowired
	private ProductPhotoMapper productPhotoMapper;

	/**
     * 查询商品图片信息
     *
     * @param id 商品图片ID
     * @return 商品图片信息
     */
	public ProductPhotoEntity findById(Long id) {
	    return productPhotoMapper.findById(id);
	}

	/**
     * 根据条件分页查询商品图片列表
     *
     * @param productPhotoConditionEntity 商品图片信息
     * @return 商品图片集合
     */
	public ResponsePageEntity<ProductPhotoEntity> searchByPage(ProductPhotoConditionEntity productPhotoConditionEntity) {
		int count = productPhotoMapper.searchCount(productPhotoConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(productPhotoConditionEntity);
		}
		List<ProductPhotoEntity> dataList = productPhotoMapper.searchByCondition(productPhotoConditionEntity);
		return ResponsePageEntity.build(productPhotoConditionEntity, count, dataList);
	}

    /**
     * 新增商品图片
     *
     * @param productPhotoEntity 商品图片信息
     * @return 结果
     */
	public int insert(ProductPhotoEntity productPhotoEntity) {
	    return productPhotoMapper.insert(productPhotoEntity);
	}

	/**
     * 修改商品图片
     *
     * @param productPhotoEntity 商品图片信息
     * @return 结果
     */
	public int update(ProductPhotoEntity productPhotoEntity) {
	    return productPhotoMapper.update(productPhotoEntity);
	}

	/**
     * 批量删除商品图片对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<ProductPhotoEntity> entities = productPhotoMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "商品图片已被删除");

		ProductPhotoEntity entity = new ProductPhotoEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return productPhotoMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return productPhotoMapper;
	}

}
