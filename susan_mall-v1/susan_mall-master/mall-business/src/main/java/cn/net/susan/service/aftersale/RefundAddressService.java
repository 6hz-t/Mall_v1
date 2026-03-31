package cn.net.susan.service.aftersale;

import java.util.List;

import cn.net.susan.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.susan.mapper.aftersale.RefundAddressMapper;
import cn.net.susan.entity.aftersale.RefundAddressConditionEntity;
import cn.net.susan.entity.aftersale.RefundAddressEntity;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.mapper.BaseMapper;

 
@Service
public class RefundAddressService extends BaseService<RefundAddressEntity, RefundAddressConditionEntity> {

	@Autowired
	private RefundAddressMapper refundAddressMapper;

	/**
     * 查询退货单地址信息
     *
     * @param id 退货单地址ID
     * @return 退货单地址信息
     */
	public RefundAddressEntity findById(Long id) {
	    return refundAddressMapper.findById(id);
	}

	/**
     * 根据条件分页查询退货单地址列表
     *
     * @param refundAddressConditionEntity 退货单地址信息
     * @return 退货单地址集合
     */
	public ResponsePageEntity<RefundAddressEntity> searchByPage(RefundAddressConditionEntity refundAddressConditionEntity) {
		return super.searchByPage(refundAddressConditionEntity);
	}

    /**
     * 新增退货单地址
     *
     * @param refundAddressEntity 退货单地址信息
     * @return 结果
     */
	public int insert(RefundAddressEntity refundAddressEntity) {
	    return refundAddressMapper.insert(refundAddressEntity);
	}

	/**
     * 修改退货单地址
     *
     * @param refundAddressEntity 退货单地址信息
     * @return 结果
     */
	public int update(RefundAddressEntity refundAddressEntity) {
	    return refundAddressMapper.update(refundAddressEntity);
	}

	/**
     * 批量删除退货单地址
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<RefundAddressEntity> entities = refundAddressMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "退货单地址已被删除");

		RefundAddressEntity entity = new RefundAddressEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return refundAddressMapper.deleteByIds(ids, entity);
	}

    @Override
    protected BaseMapper getBaseMapper() {
        return refundAddressMapper;
    }
}
