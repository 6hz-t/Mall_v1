package cn.net.susan.service.common;

import java.util.List;

import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.common.CommonPhotoMapper;
import cn.net.susan.entity.common.CommonPhotoConditionEntity;
import cn.net.susan.entity.common.CommonPhotoEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;

@Service
public class CommonPhotoService extends BaseService< CommonPhotoEntity,  CommonPhotoConditionEntity> {

	@Autowired
	private CommonPhotoMapper commonPhotoMapper;

	/**
     * 查询图片信息
     *
     * @param id 图片ID
     * @return 图片信息
     */
	public CommonPhotoEntity findById(Long id) {
	    return commonPhotoMapper.findById(id);
	}

	/**
     * 根据条件分页查询图片列表
     *
     * @param commonPhotoConditionEntity 图片信息
     * @return 图片集合
     */
	public ResponsePageEntity<CommonPhotoEntity> searchByPage(CommonPhotoConditionEntity commonPhotoConditionEntity) {
		return super.searchByPage(commonPhotoConditionEntity);
	}

    /**
     * 新增图片
     *
     * @param commonPhotoEntity 图片信息
     * @return 结果
     */
	public int insert(CommonPhotoEntity commonPhotoEntity) {
	    return commonPhotoMapper.insert(commonPhotoEntity);
	}

	/**
     * 修改图片
     *
     * @param commonPhotoEntity 图片信息
     * @return 结果
     */
	public int update(CommonPhotoEntity commonPhotoEntity) {
	    return commonPhotoMapper.update(commonPhotoEntity);
	}

	/**
     * 批量删除图片对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<CommonPhotoEntity> entities = commonPhotoMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "图片已被删除");

		CommonPhotoEntity entity = new CommonPhotoEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return commonPhotoMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return commonPhotoMapper;
	}

}
