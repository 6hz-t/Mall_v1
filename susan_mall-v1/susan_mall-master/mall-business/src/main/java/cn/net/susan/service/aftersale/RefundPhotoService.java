package cn.net.susan.service.aftersale;

import java.util.List;

import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.aftersale.RefundPhotoMapper;
import cn.net.susan.entity.aftersale.RefundPhotoConditionEntity;
import cn.net.susan.entity.aftersale.RefundPhotoEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;

 
@Service
public class RefundPhotoService extends BaseService<RefundPhotoEntity, RefundPhotoConditionEntity> {

	@Autowired
	private RefundPhotoMapper refundPhotoMapper;

	/**
     * 查询退货单图片信息
     *
     * @param id 退货单图片ID
     * @return 退货单图片信息
     */
	public RefundPhotoEntity findById(Long id) {
	    return refundPhotoMapper.findById(id);
	}

	/**
     * 根据条件分页查询退货单图片列表
     *
     * @param refundPhotoConditionEntity 退货单图片信息
     * @return 退货单图片集合
     */
	public ResponsePageEntity<RefundPhotoEntity> searchByPage(RefundPhotoConditionEntity refundPhotoConditionEntity) {
		return super.searchByPage(refundPhotoConditionEntity);
	}

    /**
     * 新增退货单图片
     *
     * @param refundPhotoEntity 退货单图片信息
     * @return 结果
     */
	public int insert(RefundPhotoEntity refundPhotoEntity) {
	    return refundPhotoMapper.insert(refundPhotoEntity);
	}

	/**
     * 修改退货单图片
     *
     * @param refundPhotoEntity 退货单图片信息
     * @return 结果
     */
	public int update(RefundPhotoEntity refundPhotoEntity) {
	    return refundPhotoMapper.update(refundPhotoEntity);
	}

	/**
     * 批量删除退货单图片
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<RefundPhotoEntity> entities = refundPhotoMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "退货单图片已被删除");

		RefundPhotoEntity entity = new RefundPhotoEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return refundPhotoMapper.deleteByIds(ids, entity);
	}

    @Override
    protected BaseMapper getBaseMapper() {
        return refundPhotoMapper;
    }
}
